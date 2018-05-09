package com.modzo.jwt.resources.admin.clients.register

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.Clients
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Shared

import static com.modzo.jwt.Urls.adminClients
import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static com.modzo.jwt.helpers.RandomDataUtil.randomClientId
import static com.modzo.jwt.helpers.RandomDataUtil.randomSecret
import static org.springframework.http.HttpStatus.*

class RegisterClientResourceSpec extends AbstractSpec {

    @Autowired
    Clients clients

    @Autowired
    PasswordEncoder passwordEncoder

    @Shared
    String adminToken

    @Shared
    String userToken

    void setup() {
        adminToken = authorizationHelper.adminAccessToken()
        userToken = authorizationHelper.userAccessToken()
    }

    void 'should create new client'() {
        given:
            String clientId = randomClientId()
            String secret = randomSecret()
            RegisterClientRequest request = new RegisterClientRequest(
                    clientId: clientId,
                    secret: secret
            )
        when:
            ResponseEntity<String> response = restTemplate.postForEntity(
                    adminClients(),
                    builder()
                            .body(request)
                            .bearer(adminToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == CREATED
            !response.body
        and:
            String uniqueId = response.headers.getLocation().path.split('/').last()

            Client client = clients.findByUniqueId(uniqueId).get()
            client.uniqueId == uniqueId
            client.clientId == clientId
            passwordEncoder.matches(secret, client.clientEncodedSecret)
            !client.secretRequired
            !client.scoped
            !client.enabled
            !client.autoApprove
            client.accessTokenValiditySeconds == 3600
            client.refreshTokenValiditySeconds == 36000
            client.authorities == [] as Set
            client.scopes == [] as Set
            client.authorizedGrantTypes == [] as Set
            client.registeredRedirectUris == [] as Set
            client.resourceIds == [] as Set
    }

    void 'should fail to validate create new client request'() {
        given:
            RegisterClientRequest request = new RegisterClientRequest(
                    clientId: null,
                    secret: null
            )
        when:
            ResponseEntity<String> response = restTemplate.postForEntity(
                    adminClients(),
                    builder()
                            .body(request)
                            .bearer(adminToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == BAD_REQUEST
        and:
            response.body.contains('NotBlank.clientId')
            response.body.contains('NotBlank.secret')
    }

    void 'not admin user should not access create new user endpoint'() {
        given:
            RegisterClientRequest request = new RegisterClientRequest(
                    clientId: randomClientId(),
                    secret: randomSecret()
            )
        when:
            ResponseEntity<String> response = restTemplate.postForEntity(
                    adminClients(),
                    builder()
                            .body(request)
                            .bearer(userToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == FORBIDDEN
            response.body.contains('access_denied')
    }
}

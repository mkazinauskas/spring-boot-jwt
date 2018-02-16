package com.modzo.jwt.resources.admin.clients.register

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.Urls
import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.Clients
import com.modzo.jwt.resources.admin.users.register.RegisterUserRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Ignore
import spock.lang.Shared

import static com.modzo.jwt.Urls.adminClients
import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
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

    def 'should create new client'() {
        given:
            String clientId = randomAlphanumeric(5)
            String secret = randomAlphanumeric(5)
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

    @Ignore
    /**
     * org.springframework.web.client.ResourceAccessException:
     * I/O error on POST request for "http://localhost:44379/api/admin/users":
     * cannot retry due to server authentication, in streaming mode;
     * nested exception is java.net.HttpRetryException:
     * cannot retry due to server authentication, in streaming mode
     * */
    def 'unauthorized user should not access create new user endpoint'() {
        when:
            ResponseEntity<String> response = restTemplate.postForEntity('/api/admin/users',
                    builder()
                            .body(new RegisterUserRequest())
                            .basic('fakeUser', 'fakePassword')
                            .build(),
                    String
            )
        then:
            response.statusCode == UNAUTHORIZED
            !response.body
    }

    def 'not admin user should not access create new user endpoint'() {
        when:
            ResponseEntity<String> response = restTemplate.postForEntity(
                    adminClients(),
                    builder()
                            .body(new RegisterUserRequest())
                            .bearer(userToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == FORBIDDEN
            response.body.contains('access_denied')
    }
}

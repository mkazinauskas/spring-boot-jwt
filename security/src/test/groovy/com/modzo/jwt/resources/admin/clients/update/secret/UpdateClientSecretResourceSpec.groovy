package com.modzo.jwt.resources.admin.clients.update.secret

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.Clients
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Shared

import static com.modzo.jwt.Urls.adminUpdateClientSecret
import static com.modzo.test.helpers.HttpEntityBuilder.builder
import static org.springframework.http.HttpMethod.PUT
import static org.springframework.http.HttpStatus.*

class UpdateClientSecretResourceSpec extends AbstractSpec {

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    Clients clients

    @Shared
    String adminToken

    @Shared
    String userToken

    void setup() {
        adminToken = authorizationHelper.adminAccessToken()
        userToken = authorizationHelper.userAccessToken()
    }

    void 'should update client secret'() {
        given:
            Client client = clientHelper.createRegisteredClient()
        and:
            UpdateClientSecretRequest updateClientSecret = new UpdateClientSecretRequest(
                    newSecret: 'newSecret'
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    adminUpdateClientSecret(client.uniqueId),
                    PUT,
                    builder()
                            .body(updateClientSecret)
                            .bearer(adminToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == OK
            !response.body

            Client updatedClient = clients.findByUniqueId(client.uniqueId).get()
            passwordEncoder.matches('newSecret', updatedClient.clientEncodedSecret)
    }

    void 'should fail update client'() {
        given:
            Client client = clientHelper.createRegisteredClient('oldSecret')
        and:
            UpdateClientSecretRequest updateClientSecret = new UpdateClientSecretRequest(
                    newSecret: null
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    adminUpdateClientSecret(client.uniqueId),
                    PUT,
                    builder()
                            .body(updateClientSecret)
                            .bearer(adminToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == BAD_REQUEST
            response.body.contains('NotBlank.newSecret')
    }

    void 'simple user should not access update client data endpoint'() {
        given:
            Client client = clientHelper.createRegisteredClient('oldSecret')
        and:
            UpdateClientSecretRequest updateClientSecret = new UpdateClientSecretRequest(
                    newSecret: 'newSecret'
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    adminUpdateClientSecret(client.uniqueId),
                    PUT,
                    builder()
                            .body(updateClientSecret)
                            .bearer(userToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == FORBIDDEN
            response.body.contains('access_denied')
    }
}

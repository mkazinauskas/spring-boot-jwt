package com.modzo.jwt.resources.admin.clients.update.secret

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.Urls
import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.Clients
import com.modzo.jwt.resources.admin.clients.update.data.UpdateClientDataRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Shared

import static com.modzo.jwt.Urls.adminUpdateClientData
import static com.modzo.jwt.Urls.adminUpdateClientSecret
import static com.modzo.jwt.domain.clients.Client.Authority.CLIENT
import static com.modzo.jwt.domain.clients.Client.GrantType.IMPLICIT
import static com.modzo.jwt.domain.clients.Client.GrantType.PASSWORD
import static com.modzo.jwt.domain.clients.Client.Scope.READ
import static com.modzo.jwt.domain.clients.Client.Scope.WRITE
import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static org.springframework.http.HttpMethod.PUT
import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.OK

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

    def 'should update client secret'() {
        given:
            Client client = clientHelper.createRegisteredClient('oldSecret')
        and:
            UpdateClientSecretRequest updateCientSecret = new UpdateClientSecretRequest(
                    oldSecret: 'oldSecret',
                    newSecret: 'newSecret'
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    adminUpdateClientSecret(client.uniqueId),
                    PUT,
                    builder()
                            .body(updateCientSecret)
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

    def 'simple user should not access update client data endpoint'() {
        given:
            Client client = clientHelper.createRegisteredClient('oldSecret')
        and:
            UpdateClientSecretRequest updateClientSecret = new UpdateClientSecretRequest(
                    oldSecret: 'oldSecret',
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

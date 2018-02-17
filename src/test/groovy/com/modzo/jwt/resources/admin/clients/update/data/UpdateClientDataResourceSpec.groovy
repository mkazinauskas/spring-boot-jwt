package com.modzo.jwt.resources.admin.clients.update.data

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.Clients
import com.modzo.jwt.resources.admin.clients.register.RegisterClientRequest
import com.modzo.jwt.resources.admin.clients.update.data.UpdateClientDataRequest
import com.modzo.jwt.resources.admin.users.register.RegisterUserRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Ignore
import spock.lang.Shared

import static com.modzo.jwt.Urls.adminClients
import static com.modzo.jwt.Urls.adminUpdateClientData
import static com.modzo.jwt.domain.clients.Client.Authority.CLIENT
import static com.modzo.jwt.domain.clients.Client.GrantType.IMPLICIT
import static com.modzo.jwt.domain.clients.Client.GrantType.PASSWORD
import static com.modzo.jwt.domain.clients.Client.Scope.READ
import static com.modzo.jwt.domain.clients.Client.Scope.WRITE
import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import static org.springframework.http.HttpMethod.PUT
import static org.springframework.http.HttpStatus.*

class UpdateClientDataResourceSpec extends AbstractSpec {

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

    def 'should update client data'() {
        given:
            Client client = clientHelper.createRegisteredClient()
        and:
            UpdateClientDataRequest updateClientDataRequest = new UpdateClientDataRequest(
                    clientId: client.clientId,
                    scoped: true,
                    secretRequired: true,
                    enabled: true,
                    autoApprove: true,
                    accessTokenValiditySeconds: 6000,
                    refreshTokenValiditySeconds: 60000,
                    authorities: [CLIENT],
                    scopes: [READ, WRITE],
                    authorizedGrantTypes: [PASSWORD, IMPLICIT],
                    registeredRedirectUris: ['http://test-redirect.com'],
                    resourceIds: ['test', 'go']
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    adminUpdateClientData(client.uniqueId),
                    PUT,
                    builder()
                            .body(updateClientDataRequest)
                            .bearer(adminToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == OK
            !response.body

            Client updatedClient = clients.findByUniqueId(client.uniqueId).get()
            updatedClient.uniqueId == client.uniqueId
            updatedClient.clientId == client.clientId
            updatedClient.secretRequired
            updatedClient.scoped
            updatedClient.enabled
            updatedClient.autoApprove
            updatedClient.accessTokenValiditySeconds == 6000
            updatedClient.refreshTokenValiditySeconds == 60000
            updatedClient.authorities == [CLIENT] as Set
            updatedClient.scopes == [READ, WRITE] as Set
            updatedClient.authorizedGrantTypes == [PASSWORD, IMPLICIT] as Set
            updatedClient.registeredRedirectUris == ['http://test-redirect.com'] as Set
            updatedClient.resourceIds == ['test', 'go'] as Set
    }

    def 'simple user should not access update client data endpoint'() {
        given:
            Client client = clientHelper.createRegisteredClient()

            UpdateClientDataRequest updateClientDataRequest = new UpdateClientDataRequest(
                    clientId: client.clientId,
                    scoped: true,
                    secretRequired: true,
                    enabled: true,
                    autoApprove: true,
                    accessTokenValiditySeconds: 6000,
                    refreshTokenValiditySeconds: 60000,
                    authorities: [CLIENT],
                    scopes: [READ, WRITE],
                    authorizedGrantTypes: [PASSWORD, IMPLICIT],
                    registeredRedirectUris: ['http://test-redirect.com'],
                    resourceIds: ['test', 'go']
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    adminUpdateClientData(client.uniqueId),
                    PUT,
                    builder()
                            .body(updateClientDataRequest)
                            .bearer(userToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == FORBIDDEN
            response.body.contains('access_denied')
    }
}

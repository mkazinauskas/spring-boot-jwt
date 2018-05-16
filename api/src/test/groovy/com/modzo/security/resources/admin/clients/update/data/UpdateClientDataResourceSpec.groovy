package com.modzo.security.resources.admin.clients.update.data

import com.modzo.AbstractSpec
import com.modzo.domain.clients.Client
import com.modzo.domain.clients.Clients
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import spock.lang.Shared

import static com.modzo.Urls.adminUpdateClientData
import static com.modzo.domain.clients.Client.Authority.CLIENT
import static com.modzo.domain.clients.Client.GrantType.IMPLICIT
import static com.modzo.domain.clients.Client.GrantType.PASSWORD
import static com.modzo.domain.clients.Client.Scope.READ
import static com.modzo.domain.clients.Client.Scope.WRITE
import static com.modzo.helpers.HttpEntityBuilder.builder
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

    void 'should update client data'() {
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

    void 'should fail to process wrong request'() {
        given:
            Client client = clientHelper.createRegisteredClient()

            UpdateClientDataRequest updateClientDataRequest = new UpdateClientDataRequest(
                    clientId: null,
                    scoped: null,
                    secretRequired: null,
                    enabled: null,
                    autoApprove: null,
                    accessTokenValiditySeconds: null,
                    refreshTokenValiditySeconds: null,
                    authorities: null,
                    scopes: null,
                    authorizedGrantTypes: null,
                    registeredRedirectUris: null,
                    resourceIds: null
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
            response.statusCode == BAD_REQUEST
            response.body.contains('NotBlank.clientId')
            response.body.contains('NotNull.scoped')
            response.body.contains('NotNull.secretRequired')
            response.body.contains('NotNull.enabled')
            response.body.contains('NotNull.autoApprove')
            response.body.contains('NotNull.accessTokenValiditySeconds')
            response.body.contains('NotNull.refreshTokenValiditySeconds')
            response.body.contains('NotNull.authorities')
            response.body.contains('NotNull.scopes')
            response.body.contains('NotNull.authorizedGrantTypes')
            response.body.contains('NotNull.registeredRedirectUris')
            response.body.contains('NotNull.resourceIds')
    }

    void 'simple user should not access update client data endpoint'() {
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

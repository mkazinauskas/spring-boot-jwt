package com.modzo.jwt.helpers

import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.Clients
import com.modzo.jwt.domain.clients.commands.CreateClient
import com.modzo.jwt.domain.clients.commands.UpdateClientData
import org.springframework.stereotype.Component

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

@Component
class ClientHelper {
    private final CreateClient.Handler createClientHandler

    private final UpdateClientData.Handler updateCLientDataHandler

    private final Clients clients

    ClientHelper(CreateClient.Handler createClientHandler,
                 UpdateClientData.Handler updateClientDataHandler,
                 Clients clients) {
        this.createClientHandler = createClientHandler
        this.updateCLientDataHandler = updateClientDataHandler
        this.clients = clients
    }

    Client createRegisteredClient() {
        String clientId = randomAlphanumeric(5)
        CreateClient.Response response = createClientHandler.handle(
                new CreateClient(clientId, randomAlphanumeric(5)))

        updateCLientDataHandler.handle(new UpdateClientData(
                uniqueId: response.uniqueId,
                clientId: clientId,
                scoped: true,
                secretRequired: true,
                enabled: true,
                autoApprove: true,
                accessTokenValiditySeconds: 3600,
                refreshTokenValiditySeconds: 36000,
                authorities: [Client.Authority.CLIENT],
                scopes: [Client.Scope.READ, Client.Scope.WRITE],
                authorizedGrantTypes: [Client.GrantType.PASSWORD],
                registeredRedirectUris: ['http://google.com'],
                resourceIds: ['resource']
        ))

        return clients.findByUniqueId(response.uniqueId).get()
    }
}

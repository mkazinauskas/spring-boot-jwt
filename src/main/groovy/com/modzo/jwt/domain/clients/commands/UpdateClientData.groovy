package com.modzo.jwt.domain.clients.commands

import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.Clients
import com.modzo.jwt.domain.clients.exceptions.ClientNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

class UpdateClientData {
    String uniqueId

    String clientId

    boolean scoped

    boolean secretRequired

    boolean enabled

    boolean autoApprove

    int accessTokenValiditySeconds

    int refreshTokenValiditySeconds

    Set<Client.Authority> authorities = []

    Set<Client.Scope> scopes = []

    Set<Client.GrantType> authorizedGrantTypes = []

    Set<String> registeredRedirectUris = []

    Set<String> resourceIds = []

    @Component
    static class Handler {
        private final Clients clients

        Handler(Clients clients) {
            this.clients = clients
        }

        @Transactional
        void handle(UpdateClientData updateClient) {
            Client client = clients.findByUniqueId(updateClient.uniqueId)
                    .orElseThrow { ClientNotFoundException.byUniqueId(updateClient.uniqueId) }
            client.clientId = updateClient.clientId
            client.scoped = updateClient.scoped
            client.secretRequired = updateClient.secretRequired
            client.enabled = updateClient.enabled
            client.autoApprove = updateClient.autoApprove
            client.accessTokenValiditySeconds = updateClient.accessTokenValiditySeconds
            client.refreshTokenValiditySeconds = updateClient.refreshTokenValiditySeconds

            client.authorities.clear()
            client.authorities.addAll(updateClient.authorities)

            client.scopes.clear()
            client.scopes.addAll(updateClient.scopes)

            client.authorizedGrantTypes.clear()
            client.authorizedGrantTypes.addAll(updateClient.authorizedGrantTypes)

            client.registeredRedirectUris.clear()
            client.registeredRedirectUris.addAll(updateClient.registeredRedirectUris)

            client.resourceIds.clear()
            client.resourceIds.addAll(updateClient.resourceIds)

            clients.save(client)
        }
    }
}

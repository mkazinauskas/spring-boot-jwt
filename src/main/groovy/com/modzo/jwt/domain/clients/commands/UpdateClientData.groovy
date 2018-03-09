package com.modzo.jwt.domain.clients.commands

import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.Clients
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.modzo.jwt.domain.DomainException.clientByUniqueIdWasNotFound

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
                    .orElseThrow { clientByUniqueIdWasNotFound(updateClient.uniqueId) }
            client.with {
                clientId = updateClient.clientId
                scoped = updateClient.scoped
                secretRequired = updateClient.secretRequired
                enabled = updateClient.enabled
                autoApprove = updateClient.autoApprove
                accessTokenValiditySeconds = updateClient.accessTokenValiditySeconds
                refreshTokenValiditySeconds = updateClient.refreshTokenValiditySeconds

                authorities.clear()
                authorities.addAll(updateClient.authorities)

                scopes.clear()
                scopes.addAll(updateClient.scopes)

                authorizedGrantTypes.clear()
                authorizedGrantTypes.addAll(updateClient.authorizedGrantTypes)

                registeredRedirectUris.clear()
                registeredRedirectUris.addAll(updateClient.registeredRedirectUris)

                resourceIds.clear()
                resourceIds.addAll(updateClient.resourceIds)
            }

            clients.save(client)
        }
    }
}

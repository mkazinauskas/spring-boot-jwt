package com.modzo.jwt.domain.clients.commands

import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.Clients
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

class CreateClient {

    String clientId

    String secret

    boolean enabled

    boolean autoApprove

    long accessTokenValiditySeconds

    long refreshTokenValiditySeconds

    Set<Client.Authority> authorities = []

    Set<Client.Scope> scopes = []

    Set<Client.GrantType> grantTypes = []

    Set<String> redirectUris = []

    @Component
    static class Handler {
        private final Clients clients

        Handler(Clients clients) {
            this.clients = clients
        }

        @Transactional
        Response handle(CreateClient createClient) {
            Client client = new Client(
                    clientId: createClient.clientId,
                    clientSecret: createClient.secret,
                    enabled: createClient.enabled,
                    autoApprove: createClient.autoApprove,
                    accessTokenValiditySeconds: createClient.accessTokenValiditySeconds,
                    refreshTokenValiditySeconds: createClient.refreshTokenValiditySeconds
            )
            client.authorities.addAll(createClient.authorities)
            client.scopes.addAll(createClient.scopes)
            client.registeredRedirectUris.addAll(createClient.redirectUris)
            client.authorizedGrantTypes.addAll(createClient.grantTypes)

            Client savedClient = clients.save(client)
            return new Response(uniqueId: savedClient.uniqueId)
        }
    }

    static class Response {
        String uniqueId
    }
}
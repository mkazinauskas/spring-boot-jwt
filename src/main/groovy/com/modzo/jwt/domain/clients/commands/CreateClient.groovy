package com.modzo.jwt.domain.clients.commands

import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.Clients
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

class CreateClient {

    String name

    String secret

    boolean enabled

    boolean autoApprove

    long accessTokenValiditySeconds

    long refreshTokenValiditySeconds

    Set<Client.Authority> authorities

    Set<Client.Scope> scopes

    Set<String> redirectUris

    @Component
    static class Handler {
        private final Clients clients

        Handler(Clients clients) {
            this.clients = clients
        }

        @Transactional
        Response handle(CreateClient createClient) {
            Client client = new Client(
                    name: createClient.name,
                    secret: createClient.secret,
                    enabled: createClient.enabled,
                    autoApprove: createClient.autoApprove,
                    accessTokenValiditySeconds: createClient.accessTokenValiditySeconds,
                    refreshTokenValiditySeconds: createClient.refreshTokenValiditySeconds
            )
            client.authorities.addAll(createClient.authorities)
            client.scopes.addAll(createClient.scopes)
            client.redirectUris.addAll(createClient.redirectUris)
            Client savedClient = clients.save(client)
            return new Response(uniqueId: savedClient.uniqueId)
        }
    }

    static class Response {
        String uniqueId
    }
}

package com.modzo.domain.clients.commands

import com.modzo.domain.clients.Client
import com.modzo.domain.clients.Clients
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

class CreateClient {

    String clientId

    String secret

    CreateClient(String clientId, String secret) {
        this.clientId = clientId
        this.secret = secret
    }

    @Component
    static class Handler {
        private final Clients clients

        private final PasswordEncoder passwordEncoder

        Handler(Clients clients, PasswordEncoder passwordEncoder) {
            this.clients = clients
            this.passwordEncoder = passwordEncoder
        }

        @Transactional
        Response handle(CreateClient createClient) {
            Client client = new Client(
                    clientId: createClient.clientId,
                    clientEncodedSecret: passwordEncoder.encode(createClient.secret),
            )
            Client savedClient = clients.save(client)
            return new Response(uniqueId: savedClient.uniqueId)
        }
    }

    static class Response {
        String uniqueId
    }
}

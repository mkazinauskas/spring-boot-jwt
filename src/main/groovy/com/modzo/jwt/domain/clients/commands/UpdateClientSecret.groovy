package com.modzo.jwt.domain.clients.commands

import com.modzo.jwt.domain.DomainException
import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.Clients

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

class UpdateClientSecret {
    String uniqueId

    String newSecret

    @Component
    static class Validator {

        private final Clients clients

        private final PasswordEncoder passwordEncoder

        Validator(Clients clients, PasswordEncoder passwordEncoder) {
            this.clients = clients
            this.passwordEncoder = passwordEncoder
        }

        void validate(UpdateClientSecret updateClientSecret) {
            clients.findByUniqueId(updateClientSecret.uniqueId).orElseThrow {
                DomainException.clientByUniqueIdWasNotFound(updateClientSecret.uniqueId)
            }
        }
    }

    @Component
    static class Handler {
        private final PasswordEncoder passwordEncoder

        private final Clients clients


        private final Validator validator

        Handler(PasswordEncoder passwordEncoder, Clients clients, Validator validator) {
            this.passwordEncoder = passwordEncoder
            this.clients = clients
            this.validator = validator
        }

        @Transactional
        void handle(UpdateClientSecret updateClient) {
            validator.validate(updateClient)
            Client client = clients.findByUniqueId(updateClient.uniqueId)
                    .orElseThrow { DomainException.clientByUniqueIdWasNotFound(updateClient.uniqueId) }

            client.clientEncodedSecret = passwordEncoder.encode(updateClient.newSecret)

            clients.save(client)
        }
    }
}

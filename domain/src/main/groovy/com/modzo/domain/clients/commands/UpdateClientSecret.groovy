package com.modzo.domain.clients.commands

import com.modzo.domain.DomainException
import com.modzo.domain.clients.Client
import com.modzo.domain.clients.Clients
import com.modzo.domain.commons.DomainPasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

class UpdateClientSecret {
    String uniqueId

    String newSecret

    @Component
    private static class Validator {

        private final Clients clients

        private final DomainPasswordEncoder passwordEncoder

        Validator(Clients clients, DomainPasswordEncoder passwordEncoder) {
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
        private final DomainPasswordEncoder passwordEncoder

        private final Clients clients

        private final Validator validator

        Handler(DomainPasswordEncoder passwordEncoder, Clients clients, Validator validator) {
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

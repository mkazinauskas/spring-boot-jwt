package com.modzo.security.resources.admin.clients.register

import com.modzo.security.domain.clients.commands.CreateClient
import org.hibernate.validator.constraints.NotBlank

class RegisterClientRequest {
    @NotBlank
    String clientId

    @NotBlank
    String secret

    CreateClient toCreateClient() {
        return new CreateClient(clientId, secret)
    }
}

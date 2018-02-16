package com.modzo.jwt.resources.admin.clients.register

import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.commands.CreateClient
import org.hibernate.validator.constraints.NotBlank
import org.hibernate.validator.constraints.NotEmpty

class RegisterClientRequest {
    @NotBlank
    String clientId

    String secret

    CreateClient toCreateClient() {
        return new CreateClient(
                clientId: clientId,
                secret: secret
        )
    }
}

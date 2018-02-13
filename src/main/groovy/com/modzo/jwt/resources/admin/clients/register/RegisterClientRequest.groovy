package com.modzo.jwt.resources.admin.clients.register

import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.commands.CreateClient
import org.hibernate.validator.constraints.NotBlank
import org.hibernate.validator.constraints.NotEmpty

class RegisterClientRequest {
    @NotBlank
    String clientId

    String secret

    boolean enabled

    boolean autoApprove

    long accessTokenValiditySeconds

    long refreshTokenValiditySeconds

    @NotEmpty
    Set<Client.Authority> authorities

    @NotEmpty
    Set<Client.Scope> scopes

    @NotEmpty
    Set<String> redirectUris

    CreateClient toCreateClient() {
        return new CreateClient(
                clientId: clientId,
                secret: secret,
                enabled: enabled,
                autoApprove: autoApprove,
                accessTokenValiditySeconds: accessTokenValiditySeconds,
                refreshTokenValiditySeconds: refreshTokenValiditySeconds,
                authorities: authorities,
                scopes: scopes,
                redirectUris: redirectUris
        )
    }
}

package com.modzo.jwt.resources.admin.clients.update.data

import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.commands.UpdateClientData
import org.hibernate.validator.constraints.NotBlank

import javax.validation.constraints.NotNull

class UpdateClientDataRequest {
    @NotBlank
    String clientId

    @NotNull
    Boolean scoped

    @NotNull
    Boolean secretRequired

    @NotNull
    Boolean enabled

    @NotNull
    Boolean autoApprove

    @NotNull
    Integer accessTokenValiditySeconds

    @NotNull
    Integer refreshTokenValiditySeconds

    @NotNull
    Set<Client.Authority> authorities

    @NotNull
    Set<Client.Scope> scopes

    @NotNull
    Set<Client.GrantType> authorizedGrantTypes

    @NotNull
    Set<String> registeredRedirectUris

    @NotNull
    Set<String> resourceIds

    UpdateClientData toUpdateClientData(String uniqueId) {
        return new UpdateClientData(
                uniqueId: uniqueId,
                clientId: clientId,
                scoped: clientId,
                secretRequired: secretRequired,
                enabled: enabled,
                autoApprove: autoApprove,
                accessTokenValiditySeconds: accessTokenValiditySeconds,
                refreshTokenValiditySeconds: refreshTokenValiditySeconds,
                authorities: authorities,
                scopes: scopes,
                authorizedGrantTypes: authorizedGrantTypes,
                registeredRedirectUris: registeredRedirectUris,
                resourceIds: resourceIds
        )
    }
}

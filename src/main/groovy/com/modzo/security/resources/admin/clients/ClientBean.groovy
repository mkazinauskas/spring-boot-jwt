package com.modzo.security.resources.admin.clients

import com.modzo.security.domain.clients.Client

class ClientBean {
    String uniqueId

    long version

    String clientId

    boolean secretRequired

    boolean scoped

    boolean enabled

    boolean autoApprove

    int accessTokenValiditySeconds

    int refreshTokenValiditySeconds

    Set<Client.Authority> authorities

    Set<Client.Scope> scopes

    Set<Client.GrantType> authorizedGrantTypes

    Set<String> registeredRedirectUris

    Set<String> resourceIds

    static ClientBean from(Client client) {
        return new ClientBean(
                uniqueId: client.uniqueId,
                version: client.version,
                clientId: client.clientId,
                secretRequired: client.secretRequired,
                scoped: client.scoped,
                enabled: client.enabled,
                autoApprove: client.autoApprove,
                accessTokenValiditySeconds: client.accessTokenValiditySeconds,
                refreshTokenValiditySeconds: client.refreshTokenValiditySeconds,
                authorities: client.authorities,
                scopes: client.scopes,
                authorizedGrantTypes: client.authorizedGrantTypes,
                registeredRedirectUris: client.registeredRedirectUris,
                resourceIds: client.resourceIds
        )
    }
}

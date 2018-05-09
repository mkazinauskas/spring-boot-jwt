package com.modzo.security.server.security

import com.modzo.security.domain.clients.Client
import com.modzo.security.domain.clients.Clients
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.provider.ClientDetails
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.ClientRegistrationException
import org.springframework.stereotype.Service

import static com.modzo.commons.domain.DomainException.clientByClientIdWasNotFound

@Service
class LocalClientDetailsService implements ClientDetailsService {

    private final Clients clients

    LocalClientDetailsService(Clients clients) {
        this.clients = clients
    }

    @Override
    ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Client client = clients.findByClientId(clientId).orElseThrow { -> clientByClientIdWasNotFound(clientId) }
        return new LocalClientDetails(
                clientId: client.clientId,
                clientSecret: client.clientEncodedSecret,
                secretRequired: client.secretRequired,
                scoped: client.scoped,
                scope: client.scopes*.type as Set,
                resourceIds: client.resourceIds,
                authorizedGrantTypes: client.authorizedGrantTypes*.type as Set,
                authorities: client.authorities*.name() as Set,
                registeredRedirectUri: client.registeredRedirectUris,
                accessTokenValiditySeconds: client.accessTokenValiditySeconds,
                refreshTokenValiditySeconds: client.refreshTokenValiditySeconds,
                autoApprove: client.autoApprove

        )
    }

    static class LocalClientDetails implements ClientDetails {
        String clientId

        String clientSecret

        boolean secretRequired

        boolean scoped

        Set<String> scope

        Set<String> resourceIds

        Set<String> authorizedGrantTypes

        Set<String> authorities

        Set<String> registeredRedirectUri

        Integer accessTokenValiditySeconds

        Integer refreshTokenValiditySeconds

        boolean autoApprove

        @Override
        Set<GrantedAuthority> getAuthorities() {
            return authorities.collect {
                new GrantedAuthority() {
                    @Override
                    String getAuthority() {
                        return it
                    }
                }
            } as Set<GrantedAuthority>
        }

        @Override
        boolean isAutoApprove(String scope) {
            return autoApprove
        }

        @Override
        Map<String, Object> getAdditionalInformation() {
            return [:]
        }
    }
}

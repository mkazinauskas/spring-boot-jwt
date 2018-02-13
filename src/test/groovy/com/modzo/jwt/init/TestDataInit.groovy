package com.modzo.jwt.init

import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.commands.CreateClient
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class TestDataInit {
    InitializingBean initClients(CreateClient.Handler handler) {
        return {
            handler.handle(new CreateClient(
                    name: 'sampleClientId',
                    secret: 'secret',
                    enabled: true,
                    autoApprove: true,
                    accessTokenValiditySeconds: 3600,
                    refreshTokenValiditySeconds: 36000,
                    authorities: [Client.Authority.CLIENT],
                    scopes: [Client.Scope.READ, Client.Scope.WRITE],
                    grantTypes: [Client.GrantType.IMPLICIT],
                    redirectUris: ['http://google.com']
            ))
        };
    }

}

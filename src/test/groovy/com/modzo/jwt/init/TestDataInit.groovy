package com.modzo.jwt.init

import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.commands.CreateClient
import groovy.transform.Immutable
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class TestDataInit {

    static final TestClient TEST_CLIENT = new TestClient('test', 'secret')

    @Bean
    InitializingBean initClients(CreateClient.Handler handler) {
        return {
            handler.handle(new CreateClient(
                    clientId: TEST_CLIENT.clientId,
                    secret: TEST_CLIENT.secret,
                    enabled: true,
                    autoApprove: true,
                    accessTokenValiditySeconds: 3600,
                    refreshTokenValiditySeconds: 36000,
                    authorities: [Client.Authority.CLIENT],
                    scopes: [Client.Scope.READ, Client.Scope.WRITE],
                    grantTypes: [Client.GrantType.PASSWORD, Client.GrantType.REFRESH_TOKEN],
                    redirectUris: ['http://google.com']
            ))
        }
    }

    @Immutable
    static class TestClient {
        String clientId
        String secret
    }
}

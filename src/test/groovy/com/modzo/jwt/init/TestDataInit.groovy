package com.modzo.jwt.init

import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.commands.CreateClient
import com.modzo.jwt.domain.clients.commands.UpdateClientData
import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.commands.CreateUser
import groovy.transform.Immutable
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

import static com.modzo.jwt.helpers.RandomDataUtil.randomEmail

@Component
class TestDataInit {

    static final TestClient TEST_CLIENT = new TestClient('test', 'secret')
    static final TestUser TEST_ADMIN_USER = new TestUser(
            randomEmail(),
            'adminPassword',
            [User.Authority.ROLE_ADMIN] as Set
    )
    static final TestUser TEST_USER = new TestUser(
            randomEmail(),
            'userPassword',
            [User.Authority.ROLE_USER] as Set
    )

    @Bean
    InitializingBean initTestClients(CreateClient.Handler createClientHandler,
                                     UpdateClientData.Handler updateClientDataHandler) {
        return {
            createClient(createClientHandler, updateClientDataHandler, TEST_CLIENT)
        }
    }

    private static void createClient(CreateClient.Handler createClientHandler,
                                     UpdateClientData.Handler updateClientDataHandler,
                                     TestClient testClient) {
        CreateClient.Response response = createClientHandler.handle(new CreateClient(
                clientId: testClient.clientId,
                secret: testClient.secret)
        )
        updateClientDataHandler.handle(new UpdateClientData(
                uniqueId: response.uniqueId,
                clientId: testClient.clientId,
                scoped: true,
                secretRequired: true,
                enabled: true,
                autoApprove: true,
                accessTokenValiditySeconds: 3600,
                refreshTokenValiditySeconds: 36000,
                authorities: [Client.Authority.CLIENT],
                scopes: [Client.Scope.READ, Client.Scope.WRITE],
                authorizedGrantTypes: [Client.GrantType.PASSWORD, Client.GrantType.REFRESH_TOKEN],
                registeredRedirectUris: ['http://google.com'],
                resourceIds: []
        ))
    }

    @Bean
    InitializingBean initTestUsers(CreateUser.Handler handler) {
        return {
            handler.handle(TEST_ADMIN_USER.asCreateUserCommand())
            handler.handle(TEST_USER.asCreateUserCommand())
        }
    }

    @Immutable
    static class TestClient {
        String clientId
        String secret
    }

    @Immutable
    static class TestUser {
        String email
        String password
        Set<User.Authority> authorities = []

        CreateUser asCreateUserCommand() {
            return new CreateUser(
                    email, password, authorities
            )
        }
    }
}

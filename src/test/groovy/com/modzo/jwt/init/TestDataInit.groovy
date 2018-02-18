package com.modzo.jwt.init

import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.commands.CreateClient
import com.modzo.jwt.domain.clients.commands.UpdateClientData
import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.commands.CreateUser
import com.modzo.jwt.domain.users.commands.UpdateUserData
import groovy.transform.Immutable
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

import static com.modzo.jwt.domain.users.User.Authority.ROLE_ADMIN
import static com.modzo.jwt.domain.users.User.Authority.ROLE_USER
import static com.modzo.jwt.helpers.RandomDataUtil.randomEmail

@Component
class TestDataInit {

    static final TestClient TEST_CLIENT = new TestClient('test', 'secret')
    static final TestUser TEST_ADMIN_USER = new TestUser(
            email: randomEmail(),
            password: 'adminPassword',
            authorities: [ROLE_ADMIN] as Set
    )
    static final TestUser TEST_USER = new TestUser(
            email: randomEmail(),
            password: 'userPassword',
            authorities: [ROLE_USER] as Set
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
                testClient.clientId,
                testClient.secret)
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
    InitializingBean initTestUsers(CreateUser.Handler createUserHandler,
                                   UpdateUserData.Handler updateUserDataHandler) {
        return {
            createUser(createUserHandler, updateUserDataHandler, TEST_ADMIN_USER)
            createUser(createUserHandler, updateUserDataHandler, TEST_USER)
        }
    }

    private static void createUser(CreateUser.Handler createUserHandler,
                                   UpdateUserData.Handler updateUserDataHandler,
                                   TestUser testUser) {
        CreateUser.Response response = createUserHandler.handle(testUser.asCreateUserCommand())
        updateUserDataHandler.handle(new UpdateUserData(
                uniqueId: response.uniqueId,
                email: testUser.email,
                enabled: true,
                accountNotExpired: true,
                credentialsNonExpired: true,
                accountNotLocked: true,
                authorities: testUser.authorities
        ))
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
                    email, password
            )
        }
    }
}

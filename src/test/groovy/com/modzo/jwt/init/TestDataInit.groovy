package com.modzo.jwt.init

import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.commands.CreateClient
import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.commands.CreateUser
import com.modzo.jwt.helpers.RandomDataUtil
import groovy.transform.Immutable
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

import static com.modzo.jwt.helpers.RandomDataUtil.randomEmail

@Component
class TestDataInit {

    static final TestClient TEST_CLIENT = new TestClient('test', 'secret')
    static final TestUser TEST_ADMIN_USER = new TestUser(randomEmail(), 'adminPassword', [User.Authority.ROLE_ADMIN] as Set)
    static final TestUser TEST_USER = new TestUser(randomEmail(), 'userPassword', [User.Authority.ROLE_ADMIN] as Set)


    @Bean
    InitializingBean initTestClients(CreateClient.Handler handler) {
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

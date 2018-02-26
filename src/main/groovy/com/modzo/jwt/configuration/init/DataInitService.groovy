package com.modzo.jwt.configuration.init

import com.modzo.jwt.domain.clients.Client
import com.modzo.jwt.domain.clients.commands.CreateClient
import com.modzo.jwt.domain.clients.commands.UpdateClientData
import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.commands.CreateUser
import com.modzo.jwt.domain.users.commands.UpdateUserData
import groovy.transform.Immutable
import org.springframework.context.annotation.Configuration

@Configuration
class DataInitService {
    private final CreateUser.Handler createUserHandler
    private final UpdateUserData.Handler updateUserDataHandler
    private final CreateClient.Handler createClientHandler
    private final UpdateClientData.Handler updateClientDataHandler

    DataInitService(CreateUser.Handler createUserHandler, UpdateUserData.Handler updateUserDataHandler,
                    CreateClient.Handler createClientHandler, UpdateClientData.Handler updateClientDataHandler) {
        this.createUserHandler = createUserHandler
        this.updateUserDataHandler = updateUserDataHandler
        this.createClientHandler = createClientHandler
        this.updateClientDataHandler = updateClientDataHandler
    }

    void createUser(TestUser testUser) {
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

    void createClient(TestClient testClient) {
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
                    true, email, password
            )
        }
    }
}

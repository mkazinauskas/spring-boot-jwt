package com.modzo.configuration

import com.modzo.jwt.init.DataInitService
import com.modzo.jwt.domain.clients.Clients
import com.modzo.jwt.domain.users.Users
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

import static com.modzo.jwt.domain.users.User.Authority.*

@Component
@ConditionalOnProperty(name = 'application.init.data', havingValue = 'true')
class InitData {

    private static final DataInitService.TestClient CLIENT = new DataInitService.TestClient('testClient', 'testSecret')

    private static final DataInitService.TestUser ADMIN_USER = new DataInitService.TestUser(
            email: 'admin@gmail.com',
            password: 'adminPassword',
            authorities: [REGISTERED_USER, ADMIN] as Set
    )
    private static final DataInitService.TestUser SIMPLE_USER = new DataInitService.TestUser(
            email: 'user@gmail.com',
            password: 'userPassword',
            authorities: [REGISTERED_USER, USER] as Set
    )
    private static final DataInitService.TestUser SIMPLE_REGISTERED_USER = new DataInitService.TestUser(
            email: 'registereduser@gmail.com',
            password: 'registeredUserPassword',
            authorities: [REGISTERED_USER] as Set
    )

    @Bean
    InitializingBean initTestClients(
            Clients clients,
            DataInitService dataInitService) {
        return {
            if (clients.count() == 0) {
                dataInitService.createClient(CLIENT)
            }
        }
    }

    @Bean
    InitializingBean initTestUsers(Users users, DataInitService dataInitService) {
        return {
            if (users.count() == 0) {
                dataInitService.createUser(ADMIN_USER)
                dataInitService.createUser(SIMPLE_USER)
                dataInitService.createUser(SIMPLE_REGISTERED_USER)
            }
        }
    }

}

package com.modzo.init

import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

import static com.modzo.domain.users.User.Authority.*
import static com.modzo.helpers.RandomDataUtil.randomEmail

@Component
class TestDataInit {

    static final DataInitService.TestClient TEST_CLIENT = new DataInitService.TestClient('test', 'secret')
    static final DataInitService.TestUser TEST_ADMIN_USER = new DataInitService.TestUser(
            email: randomEmail(),
            password: 'adminPassword',
            authorities: [ADMIN] as Set
    )
    static final DataInitService.TestUser TEST_USER = new DataInitService.TestUser(
            email: randomEmail(),
            password: 'userPassword',
            authorities: [USER] as Set
    )
    static final DataInitService.TestUser TEST_REGISTERED_USER = new DataInitService.TestUser(
            email: randomEmail(),
            password: 'registeredUserPassword',
            authorities: [REGISTERED_USER] as Set
    )

    @Bean
    InitializingBean initTestClients(DataInitService dataInitService) {
        return {
            dataInitService.createClient(TEST_CLIENT)
        }
    }

    @Bean
    InitializingBean initTestUsers(DataInitService dataInitService) {
        return {
            dataInitService.createUser(TEST_ADMIN_USER)
            dataInitService.createUser(TEST_USER)
            dataInitService.createUser(TEST_REGISTERED_USER)
        }
    }
}

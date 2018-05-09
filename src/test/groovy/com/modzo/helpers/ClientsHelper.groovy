package com.modzo.helpers

import com.modzo.security.domain.clients.Client
import com.modzo.security.domain.users.Users
import com.modzo.security.domain.users.commands.CreateUser
import org.springframework.stereotype.Component

import static com.modzo.helpers.RandomDataUtil.randomEmail
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

@Component
class ClientsHelper {
    private final CreateUser.Handler createUserHandler

    private final Users users

    ClientsHelper(CreateUser.Handler createUserHandler, Users users) {
        this.createUserHandler = createUserHandler
        this.users = users
    }

    Client createClient() {
        CreateUser.Response response = createUserHandler.handle(
                new CreateUser(true, randomEmail(), randomAlphanumeric(5))
        )
        return users.findByUniqueId(response.uniqueId).get()
    }
}

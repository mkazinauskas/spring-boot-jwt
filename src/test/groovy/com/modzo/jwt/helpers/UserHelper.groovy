package com.modzo.jwt.helpers

import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import com.modzo.jwt.domain.users.commands.CreateUser
import org.springframework.stereotype.Component

import static com.modzo.jwt.helpers.RandomDataUtil.randomEmail
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

@Component
class UserHelper {
    private final CreateUser.Handler createUserHandler

    private final Users users

    UserHelper(CreateUser.Handler createUserHandler, Users users) {
        this.createUserHandler = createUserHandler
        this.users = users
    }

    User createUser() {
        CreateUser.Response response = createUserHandler.handle(new CreateUser(randomEmail(), randomAlphanumeric(5)))
        return users.findByUniqueId(response.uniqueId).get()
    }
}

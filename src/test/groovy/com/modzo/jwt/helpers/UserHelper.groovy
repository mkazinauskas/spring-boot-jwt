package com.modzo.jwt.helpers

import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import com.modzo.jwt.domain.users.commands.CreateUser
import com.modzo.jwt.domain.users.commands.UpdateUserData
import org.springframework.stereotype.Component

import static com.modzo.jwt.domain.users.User.Authority.REGISTERED_USER
import static com.modzo.jwt.domain.users.User.Authority.ADMIN
import static com.modzo.jwt.helpers.RandomDataUtil.randomEmail
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

@Component
class UserHelper {
    private final CreateUser.Handler createUserHandler

    private final UpdateUserData.Handler updateUserDataHandler

    private final Users users

    UserHelper(CreateUser.Handler createUserHandler,
               UpdateUserData.Handler updateUserDataHandler,
               Users users) {
        this.createUserHandler = createUserHandler
        this.updateUserDataHandler = updateUserDataHandler
        this.users = users
    }

    User createRegisteredUser(boolean activated, String password = randomAlphanumeric(5)) {
        String email = randomEmail()
        CreateUser.Response response = createUserHandler.handle(
                new CreateUser(activated, email, password))

        updateData(activated, response.uniqueId, email, [REGISTERED_USER, ADMIN] as Set<User.Authority>)

        return users.findByUniqueId(response.uniqueId).get()
    }

    void changeAuthorities(User user, Set<User.Authority> authorities) {
        updateData(user.enabled, user.uniqueId, user.email, authorities)
    }

    private updateData(boolean activated, String uniqueId, String email, Set<User.Authority> authorities) {
        updateUserDataHandler.handle(new UpdateUserData(
                uniqueId: uniqueId,
                email: email,
                enabled: activated,
                accountNotExpired: true,
                credentialsNonExpired: true,
                accountNotLocked: true,
                authorities: authorities
        ))
    }
}

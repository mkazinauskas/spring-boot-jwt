package com.modzo.jwt.domain.users.commands

import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.modzo.jwt.domain.users.exceptions.UserNotFoundException.byUniqueId

class UpdateUserAuthorities {
    String uniqueId

    Set<User.Authority> roles

    UpdateUserAuthorities(String uniqueId, Set<User.Authority> roles) {
        this.uniqueId = uniqueId
        this.roles = roles
    }

    @Component
    static class Handler {
        private final Users users

        Handler(Users users) {
            this.users = users
        }

        @Transactional
        void handle(UpdateUserAuthorities createUser) {
            User user = users.findByUniqueId(createUser.uniqueId).orElseThrow { byUniqueId(createUser.uniqueId) }
            user.authorities.clear()
            user.authorities.addAll(createUser.roles)
            users.save(user)
        }
    }
}

package com.modzo.jwt.domain.commands

import com.modzo.jwt.domain.Role
import com.modzo.jwt.domain.User
import com.modzo.jwt.domain.Users
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.modzo.jwt.domain.exceptions.UserNotFoundException.byUniqueId

class UpdateUserRoles {
    String uniqueId

    Set<Role> roles

    UpdateUserRoles(String uniqueId, Set<Role> roles) {
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
        void handle(UpdateUserRoles createUser) {
            User user = users.findByUniqueId(createUser.uniqueId).orElseThrow { byUniqueId(createUser.uniqueId) }
            user.authorities.clear()
            user.authorities.addAll(createUser.roles)
            users.save(user)
        }
    }
}

package com.modzo.domain.users.commands

import com.modzo.domain.users.User
import com.modzo.domain.users.Users
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.modzo.domain.DomainException.userByUniqueIdWasNotFound

class UpdateUserData {
    String uniqueId

    String email

    boolean enabled

    boolean accountNotExpired

    boolean credentialsNonExpired

    boolean accountNotLocked

    Set<User.Authority> authorities

    @Component
    static class Handler {
        private final Users users

        Handler(Users users) {
            this.users = users
        }

        @Transactional
        void handle(UpdateUserData updateUserData) {
            User user = users.findByUniqueId(updateUserData.uniqueId)
                    .orElseThrow { userByUniqueIdWasNotFound(updateUserData.uniqueId) }
            user.enabled = updateUserData.enabled
            user.email = updateUserData.email
            user.accountNotExpired = updateUserData.accountNotExpired
            user.credentialsNonExpired = updateUserData.credentialsNonExpired
            user.accountNotLocked = updateUserData.accountNotLocked
            user.authorities.clear()
            user.authorities.addAll(updateUserData.authorities)
            users.save(user)
        }
    }
}

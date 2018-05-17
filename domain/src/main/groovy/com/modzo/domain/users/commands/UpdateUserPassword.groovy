package com.modzo.domain.users.commands

import com.modzo.domain.DomainException
import com.modzo.domain.commons.DomainPasswordEncoder
import com.modzo.domain.users.User
import com.modzo.domain.users.Users
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.modzo.domain.DomainException.userByUniqueIdWasNotFound

class UpdateUserPassword {
    String uniqueId

    String oldPassword

    String newPassword

    @Component
    private static class Validator {

        private final Users users

        private final DomainPasswordEncoder passwordEncoder

        Validator(Users users, DomainPasswordEncoder passwordEncoder) {
            this.users = users
            this.passwordEncoder = passwordEncoder
        }

        void validate(UpdateUserPassword command) {
            User user = users.findByUniqueId(command.uniqueId).orElseThrow {
                userByUniqueIdWasNotFound(command.uniqueId)
            }

            if (!passwordEncoder.matches(command.oldPassword, user.encodedPassword)) {
                throw DomainException.passwordsDoNotMatch()
            }
        }
    }

    @Component
    static class Handler {
        private final DomainPasswordEncoder passwordEncoder

        private final Users users

        private final Validator validator

        Handler(DomainPasswordEncoder passwordEncoder, Users users, Validator validator) {
            this.passwordEncoder = passwordEncoder
            this.users = users
            this.validator = validator
        }

        @Transactional
        void handle(UpdateUserPassword command) {
            validator.validate(command)
            User user = users.findByUniqueId(command.uniqueId)
                    .orElseThrow { userByUniqueIdWasNotFound(command.uniqueId) }

            user.encodedPassword = passwordEncoder.encode(command.newPassword)

            users.save(user)
        }
    }
}

package com.modzo.jwt.domain.users.commands

import com.modzo.jwt.domain.DomainException
import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.modzo.jwt.domain.DomainException.userByUniqueIdWasNotFound

class UpdateUserPassword {
    String uniqueId

    String oldPassword

    String newPassword

    @Component
    private static class Validator {

        private final Users users

        private final PasswordEncoder passwordEncoder

        Validator(Users users, PasswordEncoder passwordEncoder) {
            this.users = users
            this.passwordEncoder = passwordEncoder
        }

        void validate(UpdateUserPassword command) {
            User user = users.findByUniqueId(command.uniqueId).orElseThrow {
                userByUniqueIdWasNotFound(command.uniqueId)
            }

            if (!passwordEncoder.matches(command.oldPassword, user.encodedPassword)) {
                throw DomainException.passwordsDoNotMatch();
            }
        }
    }

    @Component
    static class Handler {
        private final PasswordEncoder passwordEncoder

        private final Users users

        private final Validator validator

        Handler(PasswordEncoder passwordEncoder, Users users, Validator validator) {
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

package com.modzo.domain.users.commands

import com.modzo.domain.commons.PasswordResetEmail
import com.modzo.domain.users.User
import com.modzo.domain.users.Users
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.modzo.domain.DomainException.userByEmailWasNotFound

class RemindUserPassword {

    final String email

    RemindUserPassword(String email) {
        this.email = email
    }

    @Component
    private static class Validator {
        private final Users users

        Validator(Users users) {
            this.users = users
        }

        void validate(RemindUserPassword command) {
            users.findByEmail(command.email)
                    .orElseThrow { userByEmailWasNotFound(command.activationCode) }
        }
    }

    @Component
    static class Handler {
        private final Users users

        private final Validator validator

        private final PasswordResetEmail passwordResetEmail

        Handler(Users users, Validator validator, PasswordResetEmail passwordResetEmail) {
            this.users = users
            this.validator = validator
            this.passwordResetEmail = passwordResetEmail
        }

        @Transactional
        void handle(RemindUserPassword command) {
            validator.validate(command)

            User user = users.findByEmail(command.email).get()
            user.newPasswordResetCode()

            passwordResetEmail.send(user.email, user.passwordResetCode)
        }
    }
}

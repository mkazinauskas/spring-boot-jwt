package com.modzo.jwt.domain.users.commands

import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import com.modzo.jwt.email.commands.SendPasswordResetEmail
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.modzo.jwt.domain.DomainException.userByEmailWasNotFound

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

        private final SendPasswordResetEmail.Handler handler

        Handler(Users users, Validator validator, SendPasswordResetEmail.Handler handler) {
            this.users = users
            this.validator = validator
            this.handler = handler
        }

        @Transactional
        void handle(RemindUserPassword command) {
            validator.validate(command)

            User user = users.findByEmail(command.email).get()
            user.newPasswordResetCode()

            handler.handle(new SendPasswordResetEmail(user.email, user.passwordResetCode))
        }
    }
}

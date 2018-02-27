package com.modzo.jwt.domain.users.commands

import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.modzo.jwt.domain.DomainException.userActivationCodeIsIncorrect
import static com.modzo.jwt.domain.DomainException.userByEmailWasNotFound
import static com.modzo.jwt.domain.DomainException.userWithActivationCodeIsAlreadyActivated
import static org.apache.commons.lang3.StringUtils.isBlank

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

        private final

        Handler(Users users, Validator validator) {
            this.users = users
            this.validator = validator
        }

        @Transactional
        void handle(RemindUserPassword command) {
            validator.validate(command)

            User user = users.findByEmail(command.email).get()
            user.newPasswordResetCode()

            //Send email
        }
    }
}

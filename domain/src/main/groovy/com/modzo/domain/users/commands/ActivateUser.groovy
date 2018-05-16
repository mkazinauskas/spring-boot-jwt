package com.modzo.domain.users.commands

import com.modzo.domain.users.User
import com.modzo.domain.users.Users
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.modzo.domain.DomainException.*
import static org.apache.commons.lang3.StringUtils.isBlank

class ActivateUser {

    final String email

    final String activationCode

    ActivateUser(String email, String activationCode) {
        this.email = email
        this.activationCode = activationCode
    }

    @Component
    private static class Validator {
        private final Users users

        Validator(Users users) {
            this.users = users
        }

        void validate(ActivateUser activateUser) {
            if (isBlank(activateUser.activationCode)) {
                throw userActivationCodeIsIncorrect(activateUser.activationCode)
            }

            if (isBlank(activateUser.email)) {
                throw userByEmailWasNotFound(activateUser.email)
            }

            User user = users.findByEmail(activateUser.email)
                    .orElseThrow { userByEmailWasNotFound(activateUser.email) }

            if (user.activationCode != activateUser.activationCode) {
                throw userActivationCodeIsIncorrect(activateUser.activationCode)
            }

            if (user.enabled) {
                userWithActivationCodeIsAlreadyActivated(activateUser.activationCode)
            }
        }
    }

    @Component
    static class Handler {
        private final Users users

        private final Validator validator

        Handler(Users users, Validator validator) {
            this.users = users
            this.validator = validator
        }

        @Transactional
        void handle(ActivateUser activateUser) {
            validator.validate(activateUser)

            User user = users.findByActivationCode(activateUser.activationCode).get()
            user.activate()
        }
    }
}

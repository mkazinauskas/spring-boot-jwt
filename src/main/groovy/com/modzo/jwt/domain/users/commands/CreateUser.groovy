package com.modzo.jwt.domain.users.commands

import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import com.modzo.jwt.email.commands.SendActivationEmail
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.modzo.jwt.domain.users.User.Authority.REGISTERED_USER

class CreateUser {

    final boolean activated

    final String email

    final String password

    CreateUser(boolean activated, String email, String password) {
        this.activated = activated
        this.email = email
        this.password = password
    }

    @Component
    static class Handler {
        private final Users users
        private final PasswordEncoder passwordEncoder
        private final SendActivationEmail.Handler sendActivationEmail

        Handler(Users users, PasswordEncoder passwordEncoder, SendActivationEmail.Handler sendActivationEmail) {
            this.users = users
            this.passwordEncoder = passwordEncoder
            this.sendActivationEmail = sendActivationEmail
        }

        @Transactional
        Response handle(CreateUser createUser) {
            User user = new User(
                    email: createUser.email,
                    encodedPassword: passwordEncoder.encode(createUser.password),
                    enabled: createUser.activated,
                    accountNotExpired: true,
                    credentialsNonExpired: true,
                    accountNotLocked: true
            )
            if (createUser.activated) {
                user.activationCode = null
            }
            user.authorities.addAll([REGISTERED_USER])
            User savedUser = users.saveAndFlush(user)
            if (!createUser.activated) {
                sendActivationEmail(savedUser)
            }
            return new Response(uniqueId: savedUser.uniqueId)
        }

        private void sendActivationEmail(User savedUser) {
            sendActivationEmail.handle(
                    new SendActivationEmail(
                            savedUser.uniqueId,
                            savedUser.email,
                            savedUser.activationCode)
            )
        }
    }

    static class Response {
        String uniqueId
    }
}

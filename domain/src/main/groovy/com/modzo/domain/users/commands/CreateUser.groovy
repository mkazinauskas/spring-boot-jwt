package com.modzo.domain.users.commands

import com.modzo.domain.commons.ActivationEmail
import com.modzo.domain.commons.DomainPasswordEncoder
import com.modzo.domain.users.User
import com.modzo.domain.users.Users
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.modzo.domain.users.User.Authority.REGISTERED_USER

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
        private final DomainPasswordEncoder passwordEncoder
        private final ActivationEmail activationEmail

        Handler(Users users, DomainPasswordEncoder passwordEncoder, ActivationEmail activationEmail) {
            this.users = users
            this.passwordEncoder = passwordEncoder
            this.activationEmail = activationEmail
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
            if (!createUser.activated) {
                user.deactivate()
            }
            user.authorities.addAll([REGISTERED_USER])
            User savedUser = users.saveAndFlush(user)
            if (!createUser.activated) {
                activationEmail.send(savedUser.email, savedUser.activationCode)
            }
            return new Response(uniqueId: savedUser.uniqueId)
        }
    }

    static class Response {
        String uniqueId
    }
}

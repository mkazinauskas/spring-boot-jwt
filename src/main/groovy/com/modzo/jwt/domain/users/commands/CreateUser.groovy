package com.modzo.jwt.domain.users.commands

import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

class CreateUser {

    String email

    String password

    CreateUser(String email, String password) {
        this.email = email
        this.password = password
    }

    @Component
    static class Handler {
        private final Users users
        private final PasswordEncoder passwordEncoder

        Handler(Users users, PasswordEncoder passwordEncoder) {
            this.users = users
            this.passwordEncoder = passwordEncoder
        }

        @Transactional
        Response handle(CreateUser createUser) {
            User user = new User(
                    email: createUser.email,
                    encodedPassword: passwordEncoder.encode(createUser.password),
                    enabled: true,
                    accountNotExpired: true,
                    credentialsNonExpired: true,
                    accountNotLocked: true
            )
            user.authorities.add(User.Authority.ROLE_REGISTERED)
            User save = users.save(user)
            return new Response(uniqueId: save.uniqueId)
        }
    }

    static class Response {
        String uniqueId
    }
}
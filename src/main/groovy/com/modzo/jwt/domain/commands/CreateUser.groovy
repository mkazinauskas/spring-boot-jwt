package com.modzo.jwt.domain.commands

import com.modzo.jwt.domain.User
import com.modzo.jwt.domain.Users
import groovy.transform.CompileStatic
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.modzo.jwt.domain.Role.ROLE_REGISTERED

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
            user.authorities.add(ROLE_REGISTERED)
            User save = users.save(user)
            return new Response(uniqueId: save.uniqueId)
        }
    }

    static class Response {
        String uniqueId
    }
}

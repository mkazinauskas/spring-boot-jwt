package com.modzo.jwt.domain.users.commands

import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.modzo.jwt.domain.users.User.Authority.ROLE_REGISTERED

class CreateUser {

    final String email

    final String password

    final Set<User.Authority> authorities = []

    CreateUser(String email, String password, Collection<User.Authority> authorities) {
        this.email = email
        this.password = password
        this.authorities.addAll(authorities)
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
            user.authorities.addAll(createUser.authorities)
            User save = users.save(user)
            return new Response(uniqueId: save.uniqueId)
        }
    }

    static class Response {
        String uniqueId
    }
}

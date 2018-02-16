package com.modzo.jwt.resources.admin.users.register

import com.modzo.jwt.domain.users.User
import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank

import javax.validation.constraints.NotNull

class RegisterUserRequest {
    @NotBlank
    @Email
    String email

    @NotBlank
    String password

    @NotNull
    Set<User.Authority> authorities
}

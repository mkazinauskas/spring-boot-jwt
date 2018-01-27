package com.modzo.jwt.server.controllers.users

import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank

class RegisterUserRequest {
    @NotBlank
    @Email
    String email
    @NotBlank
    String password
}

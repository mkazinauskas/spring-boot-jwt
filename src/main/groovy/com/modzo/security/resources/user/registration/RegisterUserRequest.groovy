package com.modzo.security.resources.user.registration

import com.modzo.security.domain.users.commands.CreateUser
import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank

class RegisterUserRequest {
    @NotBlank
    @Email
    String email

    @NotBlank
    String password

    CreateUser toCreateUser() {
        return new CreateUser(false, email, password)
    }
}

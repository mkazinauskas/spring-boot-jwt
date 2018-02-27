package com.modzo.jwt.resources.user.remind

import com.modzo.jwt.domain.users.commands.RemindUserPassword
import org.hibernate.validator.constraints.NotBlank

class RemindUserPasswordRequest {
    @NotBlank
    String email

    RemindUserPassword toRemindUserPassword() {
        return new RemindUserPassword(email)
    }
}

package com.modzo.jwt.resources.management.password

import com.modzo.jwt.domain.users.commands.UpdateUserPassword
import org.hibernate.validator.constraints.NotBlank

class UpdateUserPasswordRequest {

    @NotBlank
    String oldPassword

    @NotBlank
    String newPassword

    UpdateUserPassword toCommand(String uniqueId) {
        return new UpdateUserPassword(
                uniqueId: uniqueId,
                oldPassword: oldPassword,
                newPassword: newPassword
        )
    }
}

package com.modzo.jwt.resources.management.secret

import com.modzo.jwt.domain.clients.commands.UpdateClientSecret
import com.modzo.jwt.domain.users.commands.UpdateUserPassword
import org.hibernate.validator.constraints.NotBlank

class UpdateUserPasswordRequest {

    @NotBlank
    String oldSecret

    @NotBlank
    String newSecret

    UpdateUserPassword toCommand(String uniqueId) {
        return new UpdateUserPassword(
                uniqueId: uniqueId,
                oldPassword: oldSecret,
                newPassword: newSecret
        )
    }
}

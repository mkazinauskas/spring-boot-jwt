package com.modzo.jwt.resources.admin.clients.update.secret

import com.modzo.jwt.domain.clients.commands.UpdateClientSecret
import org.hibernate.validator.constraints.NotBlank

class UpdateClientSecretRequest {

    @NotBlank
    String newSecret

    UpdateClientSecret toUpdateClientSecret(String uniqueId) {
        return new UpdateClientSecret(
                uniqueId: uniqueId,
                newSecret: newSecret
        )
    }
}

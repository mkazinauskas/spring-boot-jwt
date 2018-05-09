package com.modzo.security.resources.admin.clients.update.secret

import com.modzo.security.domain.clients.commands.UpdateClientSecret
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

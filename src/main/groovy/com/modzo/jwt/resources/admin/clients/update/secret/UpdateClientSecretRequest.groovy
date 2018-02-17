package com.modzo.jwt.resources.admin.clients.update.secret

import com.modzo.jwt.domain.clients.commands.UpdateClientSecret
import org.hibernate.validator.constraints.NotBlank

class UpdateClientSecretRequest {

    @NotBlank
    String clientUniqueId

    @NotBlank
    String oldSecret

    @NotBlank
    String newSecret

    UpdateClientSecret toUpdateClientSecret(String uniqueId) {
        return new UpdateClientSecret(
                uniqueId: uniqueId,
                oldSecret: oldSecret,
                newSecret: newSecret
        )
    }
}

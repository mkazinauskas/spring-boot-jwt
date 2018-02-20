package com.modzo.jwt.resources.admin.clients.update.secret;

import com.modzo.jwt.domain.clients.commands.UpdateClientSecret;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
class UpdateClientSecretResource {

    private final UpdateClientSecret.Handler handler;

    public UpdateClientSecretResource(UpdateClientSecret.Handler handler) {
        this.handler = handler;
    }

    @PutMapping("/api/admin/clients/{uniqueId}/secret")
    ResponseEntity updateSecret(
            @PathVariable("uniqueId") String uniqueId,
            @Valid @RequestBody UpdateClientSecretRequest request) {
        handler.handle(request.toUpdateClientSecret(uniqueId));
        return ResponseEntity.ok().build();
    }
}

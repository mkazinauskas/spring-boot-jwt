package com.modzo.jwt.resources.admin.clients.update.data;

import com.modzo.jwt.domain.clients.commands.UpdateClientData;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UpdateClientDataResource {

    private final UpdateClientData.Handler handler;

    public UpdateClientDataResource(UpdateClientData.Handler handler) {
        this.handler = handler;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/admin/clients/{uniqueId}")
    ResponseEntity register(
            @PathVariable("uniqueId") String uniqueId,
            @RequestBody UpdateClientDataRequest request) {
        handler.handle(request.toUpdateClientData(uniqueId));
        return ResponseEntity.ok().build();
    }
}

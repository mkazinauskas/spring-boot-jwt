package com.modzo.security.resources.admin.clients.update.data;

import com.modzo.security.domain.clients.commands.UpdateClientData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
class UpdateClientDataResource {

    private final UpdateClientData.Handler handler;

    public UpdateClientDataResource(UpdateClientData.Handler handler) {
        this.handler = handler;
    }

    @PutMapping("/api/admin/clients/{uniqueId}")
    ResponseEntity updateClientData(
            @PathVariable("uniqueId") String uniqueId,
            @Valid @RequestBody UpdateClientDataRequest request) {
        handler.handle(request.toUpdateClientData(uniqueId));
        return ResponseEntity.ok().build();
    }
}

package com.modzo.jwt.resources.admin.clients.register;

import com.modzo.jwt.domain.clients.commands.CreateClient;
import com.modzo.jwt.domain.users.commands.CreateUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
class RegisterClientResource {

    private final CreateClient.Handler handler;

    public RegisterClientResource(CreateClient.Handler handler) {
        this.handler = handler;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/admin/clients")
    ResponseEntity register(@RequestBody RegisterClientRequest client) {
        CreateClient.Response response = handler.handle(client.toCreateClient());
        return ResponseEntity.created(URI.create("/api/admin/clients/" + response.getUniqueId())).build();
    }
}

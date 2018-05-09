package com.modzo.security.resources.admin.clients.register;

import com.modzo.security.domain.clients.commands.CreateClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
class RegisterClientResource {

    private final CreateClient.Handler handler;

    public RegisterClientResource(CreateClient.Handler handler) {
        this.handler = handler;
    }

    @PostMapping("/api/admin/clients")
    ResponseEntity register(@RequestBody @Valid RegisterClientRequest client) {
        CreateClient.Response response = handler.handle(client.toCreateClient());
        return ResponseEntity.created(URI.create("/api/admin/clients/" + response.getUniqueId())).build();
    }
}

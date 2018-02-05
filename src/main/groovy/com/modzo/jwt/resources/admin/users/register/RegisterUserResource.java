package com.modzo.jwt.resources.admin.users.register;

import com.modzo.jwt.domain.commands.CreateUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URL;

@RestController
class RegisterUserResource {

    private final CreateUser.Handler handler;

    public RegisterUserResource(CreateUser.Handler handler) {
        this.handler = handler;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/admin/users")
    ResponseEntity register(@RequestBody RegisterUserRequest account) {
        CreateUser.Response response = handler.handle(new CreateUser(account.getEmail(), account.getPassword()));
        ResponseEntity.created(URI.create("")).build();
        return ResponseEntity.created(URI.create("/api/admin/users/" + response.getUniqueId())).build();
    }
}

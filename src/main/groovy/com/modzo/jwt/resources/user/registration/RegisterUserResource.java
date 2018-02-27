package com.modzo.jwt.resources.user.registration;

import com.modzo.jwt.domain.users.commands.CreateUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
class RegisterUserResource {

    private final CreateUser.Handler handler;

    public RegisterUserResource(CreateUser.Handler handler) {
        this.handler = handler;
    }

    @PostMapping("/api/users")
    ResponseEntity register(@Valid @RequestBody RegisterUserRequest account) {
        CreateUser.Response response = handler.handle(
                new CreateUser(
                        false,
                        account.getEmail(),
                        account.getPassword()
                )
        );
        return ResponseEntity.created(URI.create("/")).build();
    }
}

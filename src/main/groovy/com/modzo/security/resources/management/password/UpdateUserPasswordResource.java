package com.modzo.security.resources.management.password;

import com.modzo.security.domain.users.commands.UpdateUserPassword;
import com.modzo.security.helpers.TokenHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
class UpdateUserPasswordResource {

    private final UpdateUserPassword.Handler handler;

    private final TokenHelper tokenHelper;

    public UpdateUserPasswordResource(UpdateUserPassword.Handler handler, TokenHelper tokenHelper) {
        this.handler = handler;
        this.tokenHelper = tokenHelper;
    }

    @PutMapping("/api/management/password")
    ResponseEntity updateSecret(@Valid @RequestBody UpdateUserPasswordRequest request) {
        handler.handle(request.toCommand(tokenHelper.getDetails().getUniqueId()));
        return ResponseEntity.ok().build();
    }
}

package com.modzo.jwt.resources.users.activation;

import com.modzo.jwt.domain.users.commands.ActivateUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
class ActivateUserResource {

    private final ActivateUser.Handler handler;

    ActivateUserResource(ActivateUser.Handler handler) {
        this.handler = handler;
    }

    @GetMapping(value = "/api/user/activation", params = {"activationCode"})
    @ResponseStatus(OK)
    void updateSecret(@RequestParam("activationCode") String activationCode) {
        handler.handle(new ActivateUser(activationCode));
    }
}

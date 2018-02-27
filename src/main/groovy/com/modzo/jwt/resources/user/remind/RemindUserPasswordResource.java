package com.modzo.jwt.resources.user.remind;

import com.modzo.jwt.domain.users.commands.RemindUserPassword;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
class RemindUserPasswordResource {

    private final RemindUserPassword.Handler handler;

    public RemindUserPasswordResource(RemindUserPassword.Handler handler) {
        this.handler = handler;
    }

    @PostMapping(value = "/api/user/password-reminder")
    @ResponseStatus(OK)
    void remindUserPasswordRequest(@Valid RemindUserPasswordRequest remindUserPasswordRequest) {
        handler.handle(remindUserPasswordRequest.toRemindUserPassword());
    }
}

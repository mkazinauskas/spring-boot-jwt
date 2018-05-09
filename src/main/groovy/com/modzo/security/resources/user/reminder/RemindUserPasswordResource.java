package com.modzo.security.resources.user.reminder;

import com.modzo.security.domain.users.commands.RemindUserPassword;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    void remindUserPasswordRequest(@RequestBody @Valid RemindUserPasswordRequest remindUserPasswordRequest) {
        handler.handle(remindUserPasswordRequest.toRemindUserPassword());
    }
}

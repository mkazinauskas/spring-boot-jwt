package com.modzo.configuration;

import com.modzo.domain.commons.PasswordResetEmail;
import com.modzo.email.commands.SendPasswordResetEmail;
import org.springframework.stereotype.Component;

@Component
class DefaultPasswordResetEmail implements PasswordResetEmail {

    private final SendPasswordResetEmail.Handler handler;

    DefaultPasswordResetEmail(SendPasswordResetEmail.Handler handler) {
        this.handler = handler;
    }

    @Override
    public void send(String email, String passwordResetCode) {
        handler.handle(new SendPasswordResetEmail(email, passwordResetCode));
    }
}

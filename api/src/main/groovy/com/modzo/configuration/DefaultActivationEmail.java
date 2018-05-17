package com.modzo.configuration;

import com.modzo.domain.commons.ActivationEmail;
import com.modzo.email.commands.SendActivationEmail;
import org.springframework.stereotype.Component;

@Component
class DefaultActivationEmail implements ActivationEmail {

    private final SendActivationEmail.Handler handler;

    DefaultActivationEmail(SendActivationEmail.Handler handler) {
        this.handler = handler;
    }

    @Override
    public void send(String email, String activationCode) {
        handler.handle(new SendActivationEmail(email, activationCode));
    }
}

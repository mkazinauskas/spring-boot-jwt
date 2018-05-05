package com.modzo.jwt.server.security.bruteforce;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final IpResolver ipResolver;

    private final BruteForceIPsRegister register;

    public AuthenticationSuccessEventListener(IpResolver ipResolver, BruteForceIPsRegister register) {
        this.ipResolver = ipResolver;
        this.register = register;
    }

    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        ipResolver.resolve().ifPresent(
                ip -> register.success(ip, EmailResolver.resolve(event.getAuthentication()))
        );
    }
}

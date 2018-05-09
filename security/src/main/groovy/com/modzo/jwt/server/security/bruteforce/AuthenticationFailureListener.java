package com.modzo.jwt.server.security.bruteforce;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final IpResolver ipResolver;

    private final BruteForceIPsRegister register;

    public AuthenticationFailureListener(IpResolver ipResolver, BruteForceIPsRegister register) {
        this.ipResolver = ipResolver;
        this.register = register;
    }

    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        ipResolver.resolve().ifPresent(
                ip -> register.failed(ip, EmailResolver.resolve(event.getAuthentication()))
        );


    }
}

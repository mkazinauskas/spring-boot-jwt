package com.modzo.domain.commons;

public interface ActivationEmail {
    void send(String email, String activationCode);
}

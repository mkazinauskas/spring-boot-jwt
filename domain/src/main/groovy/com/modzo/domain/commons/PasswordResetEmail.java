package com.modzo.domain.commons;

public interface PasswordResetEmail {
    void send(String email, String passwordResetCode);
}

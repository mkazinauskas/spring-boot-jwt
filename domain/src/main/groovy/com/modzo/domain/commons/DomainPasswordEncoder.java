package com.modzo.domain.commons;

public interface DomainPasswordEncoder {
    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}

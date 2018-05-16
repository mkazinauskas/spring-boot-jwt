package com.modzo.security.server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class SecurityConfiguration {
    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}

package com.modzo.jwt

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@EnableAsync
@SpringBootApplication
class JwtApplication {

    static void main(String[] args) {
        SpringApplication.run JwtApplication, args
    }

    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder()
    }

}

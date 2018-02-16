package com.modzo.jwt

import com.modzo.jwt.resources.admin.users.UserActionService
import com.modzo.jwt.resources.admin.users.register.RegisterUserRequest
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
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
    PasswordEncoder encoder(){
        return new BCryptPasswordEncoder()
    }

    @Bean
    CommandLineRunner init(UserActionService accountService) {
        return {
            ['admin@admin.com', 'user@user.com'].each
                    { username ->
                        RegisterUserRequest acct = new RegisterUserRequest()
                        acct.setEmail(username)
                        acct.setPassword("password")
                        accountService.registerUser(acct)
                    }
        }
    }
}

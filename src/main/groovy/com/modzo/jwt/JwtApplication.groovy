package com.modzo.jwt

import com.modzo.jwt.server.controllers.users.UserActionService
import com.modzo.jwt.server.controllers.users.RegisterUserRequest
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

import javax.sql.DataSource

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

//    @Bean
//    @Qualifier("mainDataSource")
//    DataSource dataSource() {
//        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder()
//        EmbeddedDatabase db = builder
//                .setType(EmbeddedDatabaseType.H2)
//                .build()
//        return db
//    }

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

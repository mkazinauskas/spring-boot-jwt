package com.modzo.configuration

import com.modzo.test.helpers.StubJavaMailSender
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.mail.javamail.JavaMailSender

@Configuration
class ApiTestConfiguration {

    @Bean
    @Primary
    JavaMailSender stubJavaMailSender() {
        new StubJavaMailSender()
    }
}

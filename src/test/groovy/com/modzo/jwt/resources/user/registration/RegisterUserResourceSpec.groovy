package com.modzo.jwt.resources.user.registration

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import com.modzo.jwt.helpers.StubJavaMailSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.mail.SimpleMailMessage
import org.springframework.security.crypto.password.PasswordEncoder

import static com.modzo.jwt.Urls.registerUser
import static com.modzo.jwt.domain.users.User.Authority.REGISTERED_USER
import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.CREATED

class RegisterUserResourceSpec extends AbstractSpec {

    @Autowired
    Users users

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    StubJavaMailSender mailSender

    void 'should register new user'() {
        given:
            String randomString = randomAlphanumeric(5)
            RegisterUserRequest request = new RegisterUserRequest(
                    email: "${randomString}@${randomString}.com",
                    password: randomString
            )
        when:
            ResponseEntity<String> response = restTemplate.postForEntity(registerUser(),
                    builder()
                            .body(request)
                            .build(),
                    String
            )
        then:
            response.statusCode == CREATED
            !response.body
        and:
            User user = users.findByEmail(request.email).get()
            user.email == request.email
            passwordEncoder.matches(request.password, user.encodedPassword)
            user.authorities == [REGISTERED_USER] as Set
            user.encodedPassword
            user.accountNotLocked
            user.credentialsNonExpired
            !user.enabled
        and:
            SimpleMailMessage message = mailSender.sentSimpleMailMessages.find { it.to.first() == user.email }
            message.subject == 'User activation'
            message.text == '<h1>Hi,</h1>\n' +
                    "<p>Your activation code is <b>${user.activationCode}</b></p>" +
                    '\n\n<p>Best regards!</p>'
    }

    void 'should fail register new user validation'() {
        given:
            RegisterUserRequest request = new RegisterUserRequest(
                    email: null,
                    password: null
            )
        when:
            ResponseEntity<String> response = restTemplate.postForEntity(registerUser(),
                    builder()
                            .body(request)
                            .build(),
                    String
            )
        then:
            response.statusCode == BAD_REQUEST
            response.body.contains('NotBlank.email')
            response.body.contains('NotBlank.password')
    }
}
package com.modzo.jwt.resources.admin.users.register

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Ignore
import spock.lang.Shared

import static com.modzo.jwt.domain.users.User.Authority.REGISTERED_USER
import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import static org.springframework.http.HttpStatus.*

class RegisterUserResourceSpec extends AbstractSpec {

    @Autowired
    Users users

    @Autowired
    PasswordEncoder passwordEncoder

    @Shared
    String adminToken

    @Shared
    String userToken

    void setup() {
        adminToken = authorizationHelper.adminAccessToken()
        userToken = authorizationHelper.userAccessToken()
    }

    def 'should register new user'() {
        given:
            String randomString = randomAlphanumeric(5)
            RegisterUserRequest request = new RegisterUserRequest(
                    email: "${randomString}@${randomString}.com",
                    password: randomString
            )
        when:
            ResponseEntity<String> response = restTemplate.postForEntity('/api/admin/users',
                    builder()
                            .body(request)
                            .bearer(adminToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == CREATED
            !response.body
        and:
            String uniqueId = response.headers.getLocation().path.split('/').last()

            User user = users.findByUniqueId(uniqueId).get()
            user.email == request.email
            passwordEncoder.matches(request.password, user.encodedPassword)
            user.authorities == [REGISTERED_USER] as Set
            user.encodedPassword
            user.accountNotLocked
            user.credentialsNonExpired
            user.enabled
    }

    def 'should fail register new user validation'() {
        given:
            RegisterUserRequest request = new RegisterUserRequest(
                    email: null,
                    password: null
            )
        when:
            ResponseEntity<String> response = restTemplate.postForEntity('/api/admin/users',
                    builder()
                            .body(request)
                            .bearer(adminToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == BAD_REQUEST
            response.body.contains('NotBlank.email')
            response.body.contains('NotBlank.password')
    }

    def 'not admin user should not access register new user endpoint'() {
        when:
            ResponseEntity<String> response = restTemplate.postForEntity('/api/admin/users',
                    builder()
                            .body(new RegisterUserRequest())
                            .bearer(userToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == FORBIDDEN
            response.body.contains('access_denied')
    }
}


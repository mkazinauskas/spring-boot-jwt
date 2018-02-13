package com.modzo.jwt.resources.admin.users.register

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Ignore
import spock.lang.Shared

import static com.modzo.jwt.domain.users.User.Authority.ROLE_REGISTERED
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

    def 'should create new user'() {
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
            user.authorities == [ROLE_REGISTERED] as Set
            user.encodedPassword
            user.accountNotLocked
            user.credentialsNonExpired
            user.enabled
    }

    @Ignore
    /**
     * org.springframework.web.client.ResourceAccessException:
     * I/O error on POST request for "http://localhost:44379/api/admin/users":
     * cannot retry due to server authentication, in streaming mode;
     * nested exception is java.net.HttpRetryException:
     * cannot retry due to server authentication, in streaming mode
     * */
    def 'unauthorized user should not access create new user endpoint'() {
        when:
            ResponseEntity<String> response = restTemplate.postForEntity('/api/admin/users',
                    builder()
                            .body(new RegisterUserRequest())
                            .basic('fakeUser', 'fakePassword')
                            .build(),
                    String
            )
        then:
            response.statusCode == UNAUTHORIZED
            !response.body
    }

    def 'not admin user should not access create new user endpoint'() {
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

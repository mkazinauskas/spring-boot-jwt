package com.modzo.jwt.resources.admin.users.register

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.domain.Role
import com.modzo.jwt.domain.User
import com.modzo.jwt.domain.Users
import com.modzo.jwt.helpers.AuthorizationHelper
import com.modzo.jwt.helpers.TokenRestTemplate
import groovy.json.JsonOutput
import org.apache.commons.lang3.RandomStringUtils
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Shared

import static com.modzo.jwt.domain.Role.ROLE_REGISTERED
import static com.modzo.jwt.helpers.AuthorizationHelper.asAuthorizationHeader
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK

class RegisterUserResourceSpec extends AbstractSpec {

    @Autowired
    TokenRestTemplate tokenTemplate

    @Autowired
    AuthorizationHelper authorizationHelper

    @Autowired
    Users users

    @Autowired
    PasswordEncoder passwordEncoder

    @Shared
    String adminToken

    void setup() {
        adminToken = authorizationHelper.adminToken()
    }

    def 'should open protected endpoint '() {
        given:
            String randomString = randomAlphanumeric(5)
            RegisterUserRequest request = new RegisterUserRequest(
                    email: "${randomString}@${randomString}.com",
                    password: randomString
            )
        when:
//            ResponseEntity<String> response = tokenTemplate.post('/api/admin/users',
//                    request,
//                    asAuthorizationHeader(adminToken),
//                    String
//            )
            ResponseEntity response = tokenTemplate.post('/api/admin/users',
                    [  email: "${randomString}@${randomString}.com",
                       password: randomString],
                    asAuthorizationHeader(adminToken)
            )
        then:
            response.statusCode == CREATED
            !response.body
        and:
            String uniqueId =response.headers.getLocation().path.split('/').last()

            User user = users.findByUniqueId(uniqueId).get()
            user.email == request.email
            passwordEncoder.matches(request.password, user.encodedPassword)
            user.authorities == [ROLE_REGISTERED] as Set
            user.encodedPassword
            user.accountNotLocked
            user.credentialsNonExpired
            user.enabled
    }

}

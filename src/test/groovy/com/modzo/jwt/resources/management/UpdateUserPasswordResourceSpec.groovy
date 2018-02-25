package com.modzo.jwt.resources.management

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.Urls
import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import com.modzo.jwt.helpers.HttpEntityBuilder
import com.modzo.jwt.resources.management.secret.UpdateUserPasswordRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder

import static org.springframework.http.HttpMethod.PUT
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.OK

class UpdateUserPasswordResourceSpec extends AbstractSpec {

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    Users users

    def 'should update user password'() {
        given:
            User user = userHelper.createRegisteredUser('oldSecret')
        and:
            UpdateUserPasswordRequest request = new UpdateUserPasswordRequest(
                    oldSecret: 'oldSecret',
                    newSecret: 'newSecret'
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    Urls.managementUpdatePassword(),
                    PUT,
                    HttpEntityBuilder.builder()
                            .body(request)
                            .bearer(authorizationHelper.getToken(user.email, 'oldSecret').accessToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == OK
            !response.body

            User updatedUser = users.findByUniqueId(user.uniqueId).get()
            passwordEncoder.matches('newSecret', updatedUser.encodedPassword)
    }

    def 'should fail to update user password'() {
        given:
            User user = userHelper.createRegisteredUser('oldSecret')
        and:
            UpdateUserPasswordRequest request = new UpdateUserPasswordRequest(
                    oldSecret: null,
                    newSecret: null
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    Urls.managementUpdatePassword(),
                    PUT,
                    HttpEntityBuilder.builder()
                            .body(request)
                            .bearer(authorizationHelper.getToken(user.email, 'oldSecret').accessToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == BAD_REQUEST
            response.body.contains('NotBlank.oldSecret')
            response.body.contains('NotBlank.newSecret')
    }

    def 'user without roles should not access change password endpoint'() {
        given:
            User user = userHelper.createRegisteredUser('oldSecret')
        and:
            userHelper.changeAuthorities(user, [] as Set)
        and:
            UpdateUserPasswordRequest request = new UpdateUserPasswordRequest(
                    oldSecret: 'oldSecret',
                    newSecret: 'newSecret'
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    Urls.managementUpdatePassword(),
                    PUT,
                    HttpEntityBuilder.builder()
                            .body(request)
                            .bearer(authorizationHelper.getToken(user.email, 'oldSecret').accessToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == FORBIDDEN
            response.body.contains('access_denied')
    }
}

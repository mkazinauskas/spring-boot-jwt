package com.modzo.security.resources.management

import com.modzo.AbstractSpec
import com.modzo.Urls
import com.modzo.domain.users.User
import com.modzo.domain.users.Users
import com.modzo.helpers.HttpEntityBuilder
import com.modzo.security.resources.management.password.UpdateUserPasswordRequest
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

    void 'should update user password'() {
        given:
            User user = userHelper.createRegisteredUser(true, 'oldPassword')
        and:
            UpdateUserPasswordRequest request = new UpdateUserPasswordRequest(
                    oldPassword: 'oldPassword',
                    newPassword: 'newPassword'
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    Urls.managementUpdatePassword(),
                    PUT,
                    HttpEntityBuilder.builder()
                            .body(request)
                            .bearer(authorizationHelper.getToken(user.email, 'oldPassword').accessToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == OK
            !response.body

            User updatedUser = users.findByUniqueId(user.uniqueId).get()
            passwordEncoder.matches('newPassword', updatedUser.encodedPassword)
    }

    void 'should fail to update user password'() {
        given:
            User user = userHelper.createRegisteredUser(true, 'oldPassword')
        and:
            UpdateUserPasswordRequest request = new UpdateUserPasswordRequest(
                    oldPassword: null,
                    newPassword: null
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    Urls.managementUpdatePassword(),
                    PUT,
                    HttpEntityBuilder.builder()
                            .body(request)
                            .bearer(authorizationHelper.getToken(user.email, 'oldPassword').accessToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == BAD_REQUEST
            response.body.contains('NotBlank.oldPassword')
            response.body.contains('NotBlank.newPassword')
    }

    void 'user without roles should not access change password endpoint'() {
        given:
            User user = userHelper.createRegisteredUser(true, 'oldPassword')
        and:
            userHelper.changeAuthorities(user, [] as Set)
        and:
            UpdateUserPasswordRequest request = new UpdateUserPasswordRequest(
                    oldPassword: 'oldPassword',
                    newPassword: 'newPassword'
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    Urls.managementUpdatePassword(),
                    PUT,
                    HttpEntityBuilder.builder()
                            .body(request)
                            .bearer(authorizationHelper.getToken(user.email, 'oldPassword').accessToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == FORBIDDEN
            response.body.contains('access_denied')
    }
}

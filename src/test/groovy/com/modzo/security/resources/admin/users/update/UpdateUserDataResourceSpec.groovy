package com.modzo.security.resources.admin.users.update

import com.modzo.AbstractSpec
import com.modzo.security.domain.users.User
import com.modzo.security.domain.users.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import spock.lang.Shared

import static com.modzo.Urls.adminUserData
import static com.modzo.security.domain.users.User.Authority.*
import static com.modzo.helpers.HttpEntityBuilder.builder
import static org.springframework.http.HttpMethod.PUT
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.NO_CONTENT

class UpdateUserDataResourceSpec extends AbstractSpec {

    @Autowired
    Users users

    @Shared
    String adminToken

    @Shared
    String userToken

    void setup() {
        adminToken = authorizationHelper.adminAccessToken()
        userToken = authorizationHelper.userAccessToken()
    }

    void 'should update user data'() {
        given:
            User newUser = userHelper.createRegisteredUser(false)
            UpdateUserDataRequest request = new UpdateUserDataRequest(
                    email: 'newemail@github.com',
                    enabled: true,
                    accountNotExpired: true,
                    credentialsNonExpired: true,
                    accountNotLocked: true,
                    authorities: [REGISTERED_USER, ADMIN]
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    adminUserData(newUser.uniqueId),
                    PUT,
                    builder()
                            .body(request)
                            .bearer(adminToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == NO_CONTENT
            !response.body

            User updatedUser = users.findByUniqueId(newUser.uniqueId).get()
            updatedUser.email == 'newemail@github.com'
            updatedUser.enabled
            updatedUser.accountNotExpired
            updatedUser.credentialsNonExpired
            updatedUser.accountNotLocked
            updatedUser.authorities.contains(REGISTERED_USER)
            updatedUser.authorities.contains(ADMIN)
    }

    void 'should fail to update user data'() {
        given:
            User newUser = userHelper.createRegisteredUser(false)
            UpdateUserDataRequest request = new UpdateUserDataRequest(
                    email: null,
                    enabled: null,
                    accountNotExpired: null,
                    credentialsNonExpired: null,
                    accountNotLocked: null,
                    authorities: null
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    adminUserData(newUser.uniqueId),
                    PUT,
                    builder()
                            .body(request)
                            .bearer(adminToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == BAD_REQUEST
            response.body.contains('NotBlank.email')
            response.body.contains('NotNull.enabled')
            response.body.contains('NotNull.accountNotExpired')
            response.body.contains('NotNull.credentialsNonExpired')
            response.body.contains('NotNull.accountNotLocked')
            response.body.contains('NotEmpty.authorities')
    }

    void 'simple user should not access update user data endpoint'() {
        given:
            User newUser = userHelper.createRegisteredUser(false)
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    adminUserData(newUser.uniqueId),
                    PUT,
                    builder()
                            .body(new UpdateUserDataRequest())
                            .bearer(userToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == FORBIDDEN
            response.body.contains('access_denied')
    }
}

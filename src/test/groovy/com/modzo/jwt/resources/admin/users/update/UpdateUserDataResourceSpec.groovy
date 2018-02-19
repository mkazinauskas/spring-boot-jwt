package com.modzo.jwt.resources.admin.users.update

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import spock.lang.Shared

import static com.modzo.jwt.Urls.adminUserData
import static com.modzo.jwt.domain.users.User.Authority.*
import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static org.springframework.http.HttpMethod.PUT
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

    def 'should update user data'() {
        given:
            User newUser = userHelper.createRegisteredUser()
            UpdateUserDataRequest request = new UpdateUserDataRequest(
                    email: 'newemail@github.com',
                    enabled: true,
                    accountNotExpired: true,
                    credentialsNonExpired: true,
                    accountNotLocked: true,
                    authorities: [REGISTERED_USER, ROLE_ADMIN]
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
            updatedUser.authorities.contains(ROLE_ADMIN)
    }

    def 'not admin user should not access update user data endpoint'() {
        given:
            User newUser = userHelper.createRegisteredUser()
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

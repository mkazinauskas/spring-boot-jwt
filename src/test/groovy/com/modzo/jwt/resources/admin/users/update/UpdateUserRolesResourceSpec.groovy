package com.modzo.jwt.resources.admin.users.update

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.domain.User
import com.modzo.jwt.domain.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import spock.lang.Shared

import static com.modzo.jwt.domain.Role.*
import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static org.springframework.http.HttpMethod.PUT
import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.NO_CONTENT

class UpdateUserRolesResourceSpec extends AbstractSpec {

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

    def 'should update user roles'() {
        given:
            User newUser = userHelper.createUser()
            UpdateUserRolesRequest request = new UpdateUserRolesRequest(
                    roles: [ROLE_REGISTERED, ROLE_USER, ROLE_ADMIN]
            )
        when:
            ResponseEntity<String> response = restTemplate.exchange("/api/admin/users/${newUser.uniqueId}/roles",
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
            updatedUser.authorities.contains(ROLE_REGISTERED)
            updatedUser.authorities.contains(ROLE_USER)
            updatedUser.authorities.contains(ROLE_ADMIN)
    }

    def 'not admin user should not access create new user endpoint'() {
        when:
            ResponseEntity<String> response = restTemplate.exchange("/api/admin/users/123/roles",
                    PUT,
                    builder()
                            .body(new UpdateUserRolesRequest())
                            .bearer(userToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == FORBIDDEN
            response.body.contains('access_denied')
    }
}

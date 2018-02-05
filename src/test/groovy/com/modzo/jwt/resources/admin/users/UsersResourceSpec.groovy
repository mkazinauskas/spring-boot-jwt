package com.modzo.jwt.resources.admin.users

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.domain.User
import com.modzo.jwt.domain.Users
import com.modzo.jwt.helpers.AuthorizationHelper
import com.modzo.jwt.helpers.UserHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import spock.lang.Shared

import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.OK

class UsersResourceSpec extends AbstractSpec {

    @Autowired
    AuthorizationHelper authorizationHelper

    @Autowired
    Users users

    @Autowired
    UserHelper userHelper

    @Shared
    String adminToken

    @Shared
    String userToken

    void setup() {
        adminToken = authorizationHelper.adminToken()
        userToken = authorizationHelper.userToken()
    }

    def 'should retrieve created user'() {
        given:
            User user = userHelper.createUser()
        when:
            ResponseEntity<UserBean> response = restTemplate.exchange("/api/admin/users/${user.uniqueId}",
                    GET,
                    builder()
                            .bearer(adminToken)
                            .build(),
                    UserBean
            )
        then:
            response.statusCode == OK
            response.body
            response.body.uniqueId == user.uniqueId
            response.body.email == user.email
            response.body.enabled == user.enabled
            response.body.credentialsNonExpired == user.credentialsNonExpired
            response.body.accountNotLocked == user.accountNotLocked
            response.body.accountNotExpired == user.accountNotExpired
    }

    def 'created user should not be visible for not admin'() {
        given:
            User user = userHelper.createUser()
        when:
            ResponseEntity<String> response = restTemplate.exchange("/api/admin/users/${user.uniqueId}",
                    GET,
                    builder()
                            .bearer(userToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == FORBIDDEN
            response.body.contains('access_denied')
    }
}

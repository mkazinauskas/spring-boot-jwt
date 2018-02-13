package com.modzo.jwt.resources.admin.users

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import com.modzo.jwt.helpers.PageWrapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import spock.lang.Shared

import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.OK

class UsersResourceSpec extends AbstractSpec {

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
            response.body.authorities == user.authorities*.name().toSet()
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

    def 'should list all users'() {
        given:
            userHelper.createUser()
        when:
            ResponseEntity<PageWrapper<UserBean>> response = restTemplate.exchange("/api/admin/users",
                    GET,
                    builder()
                            .bearer(adminToken)
                            .build(),
                    new ParameterizedTypeReference<PageWrapper<UserBean>>() {}
            )
        then:
            response.statusCode == OK
            response.body.size > 1
            response.body.content.first().uniqueId
            response.body.content.first().email
    }

    def 'users list should not be visible for not admin'() {
        when:
            ResponseEntity<String> response = restTemplate.exchange("/api/admin/users",
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

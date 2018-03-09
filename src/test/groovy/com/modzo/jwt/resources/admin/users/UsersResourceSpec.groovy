package com.modzo.jwt.resources.admin.users

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import com.modzo.jwt.helpers.PageWrapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import spock.lang.Shared

import static com.modzo.jwt.Urls.adminUser
import static com.modzo.jwt.Urls.adminUsers
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

    void 'should retrieve created user'() {
        given:
            User user = userHelper.createRegisteredUser(false)
        when:
            ResponseEntity<UserBean> response = restTemplate.exchange(
                    adminUser(user.uniqueId),
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
            response.body.authorities == user.authorities
    }

    void 'created user should not be visible for not admin'() {
        given:
            User user = userHelper.createRegisteredUser(false)
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    adminUser(user.uniqueId),
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

    void 'should list all users'() {
        given:
            userHelper.createRegisteredUser(false)
        when:
            ResponseEntity<PageWrapper<UserBean>> response = restTemplate.exchange(
                    adminUsers(),
                    GET,
                    builder()
                            .bearer(adminToken)
                            .build(),
                    new ParameterizedTypeReference<PageWrapper<UserBean>>() {}
            )
        then:
            response.statusCode == OK
            response.body.size > 1
        and:
            UserBean responseUser = response.body.content.first()
            User currentUser = users.findByUniqueId(responseUser.uniqueId).get()
            responseUser.uniqueId == currentUser.uniqueId
            responseUser.email == currentUser.email
            responseUser.enabled == currentUser.enabled
            responseUser.credentialsNonExpired == currentUser.credentialsNonExpired
            responseUser.accountNotLocked == currentUser.accountNotLocked
            responseUser.accountNotExpired == currentUser.accountNotExpired
            responseUser.authorities == currentUser.authorities
    }

    void 'users list should not be visible for not admin'() {
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    adminUsers(),
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
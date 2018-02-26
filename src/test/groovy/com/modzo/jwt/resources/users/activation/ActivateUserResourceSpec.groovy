package com.modzo.jwt.resources.users.activation

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity

import static com.modzo.jwt.Urls.activateUser
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.OK

class ActivateUserResourceSpec extends AbstractSpec {

    @Autowired
    Users users

    def 'should activate registered user'() {
        given:
            User registeredUser = userHelper.createRegisteredUser()
        when:
            ResponseEntity<String> response = restTemplate.getForEntity(
                    activateUser(registeredUser.activationCode),
                    String
            )
        then:
            response.statusCode == OK
        and:
            User activatedUser = users.findByUniqueId(registeredUser.uniqueId).get()
            activatedUser.activationCode == null
            activatedUser.enabled
    }

    def 'should fail activate user'() {
        when:
            ResponseEntity<String> response = restTemplate.getForEntity(
                    activateUser(null),
                    String
            )
        then:
            response.statusCode == BAD_REQUEST
    }
}
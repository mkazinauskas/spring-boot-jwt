package com.modzo.security.resources.user.activation

import com.modzo.AbstractSpec
import com.modzo.domain.users.User
import com.modzo.domain.users.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity

import static com.modzo.Urls.activateUser
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.OK

class ActivateUserResourceSpec extends AbstractSpec {

    @Autowired
    Users users

    void 'should activate registered user'() {
        given:
            User registeredUser = userHelper.createRegisteredUser(false)
        when:
            ResponseEntity<String> response = restTemplate.getForEntity(
                    activateUser(registeredUser.email, registeredUser.activationCode),
                    String
            )
        then:
            response.statusCode == OK
        and:
            User activatedUser = users.findByUniqueId(registeredUser.uniqueId).get()
            activatedUser.activationCode == null
            activatedUser.enabled
    }

    void 'should fail activate activated user'() {
        given:
            User registeredUser = userHelper.createRegisteredUser(true)
        when:
            ResponseEntity<String> response = restTemplate.getForEntity(
                    activateUser(registeredUser.email, registeredUser.activationCode),
                    String
            )
        then:
            response.statusCode == BAD_REQUEST
        and:
            User activatedUser = users.findByUniqueId(registeredUser.uniqueId).get()
            activatedUser.activationCode == null
            activatedUser.enabled
    }

    void 'should fail activate user'() {
        when:
            ResponseEntity<String> response = restTemplate.getForEntity(
                    activateUser(null, null),
                    String
            )
        then:
            response.statusCode == BAD_REQUEST
    }
}
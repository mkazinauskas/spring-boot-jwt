package com.modzo.jwt.server

import com.modzo.jwt.helpers.AuthorizationHelper
import com.modzo.jwt.helpers.TokenRestTemplate
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TokenIssueSpec extends Specification {
    @Autowired
    TokenRestTemplate tokenTemplate

    def 'should create token for client'() {
        when:
            ResponseEntity response = tokenTemplate.post('/oauth/token?grant_type=password&client_id=fooClientIdPassword&username=admin&password=nimda',
                    [:],
                    AuthorizationHelper.getAuthorizationHeaders('fooClientIdPassword', 'secret')
            )
        then:
            response.statusCode == HttpStatus.OK
        and:
            def body = new JsonSlurper().parseText(response.body)
            body.access_token.length() > 0
    }

    def 'should fail to create token for client'() {
        when:
            ResponseEntity response = tokenTemplate.post('/oauth/token?grant_type=password&client_id=fooClientIdPassword&username=admin&password=wrongPassword',
                    [:],
                    AuthorizationHelper.getAuthorizationHeaders('fooClientIdPassword', 'secret')
            )
        then:
            response.statusCode == HttpStatus.BAD_REQUEST
    }
}

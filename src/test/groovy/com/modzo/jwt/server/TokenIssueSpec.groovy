package com.modzo.jwt.server

import com.modzo.jwt.AbstractSpec
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import static com.modzo.jwt.helpers.HttpEntityBuilder.builder

class TokenIssueSpec extends AbstractSpec {

    def 'should create token for client'() {
        when:
            ResponseEntity response = restTemplate.postForEntity('/oauth/token?grant_type=password&client_id=fooClientIdPassword&username=admin&password=nimda',
                    builder()
                            .basic('fooClientIdPassword', 'secret')
                            .build(),
                    String
            )
        then:
            response.statusCode == HttpStatus.OK
        and:
            def body = new JsonSlurper().parseText(response.body)
            body.access_token.length() > 0
    }

    def 'should fail to create token for client'() {
        when:
            ResponseEntity response = restTemplate.postForEntity('/oauth/token?grant_type=password&client_id=fooClientIdPassword&username=admin&password=wrongPassword',
                    builder()
                            .basic('fooClientIdPassword', 'secret')
                            .build(),
                    String
            )
        then:
            response.statusCode == HttpStatus.BAD_REQUEST
    }
}

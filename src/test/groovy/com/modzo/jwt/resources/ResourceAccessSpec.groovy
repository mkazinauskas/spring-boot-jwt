package com.modzo.jwt.resources

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.helpers.AuthorizationHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import spock.lang.Shared

import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNAUTHORIZED

class ResourceAccessSpec extends AbstractSpec {

    @Autowired
    AuthorizationHelper authorizationHelper

    @Shared
    String adminToken

    void setup() {
        adminToken = authorizationHelper.adminToken()
    }

    def 'should open protected endpoint '() {
        when:
            ResponseEntity<String> response = restTemplate.exchange('/employee?email=test@mail.com',
                    GET,
                    builder()
                            .bearer(adminToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == OK
            response.body.startsWith 'admin'
    }

    def 'should not open protected endpoint '() {
        when:
            ResponseEntity<String> response = restTemplate.getForEntity('/employee?email=test@mail.com', String)
        then:
            response.statusCode == UNAUTHORIZED
            response.body == '{"error":"unauthorized","error_description":"Full authentication is required to access this resource"}'
    }
}

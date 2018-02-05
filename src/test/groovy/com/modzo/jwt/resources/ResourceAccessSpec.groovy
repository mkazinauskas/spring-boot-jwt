package com.modzo.jwt.resources

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.helpers.AuthorizationHelper
import com.modzo.jwt.helpers.TokenRestTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static com.modzo.jwt.helpers.AuthorizationHelper.asAuthorizationHeader
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNAUTHORIZED

class ResourceAccessSpec extends AbstractSpec {

    @Autowired
    TokenRestTemplate tokenTemplate

    @Autowired
    AuthorizationHelper authorizationHelper

    def 'should open protected endpoint '() {
        when:
            ResponseEntity response = tokenTemplate.get('/employee?email=test@mail.com',
                    asAuthorizationHeader(authorizationHelper.getToken('admin', 'nimda'))
            )
        then:
            response.statusCode == OK
            response.body.startsWith 'admin'
    }

    def 'should not open protected endpoint '() {
        when:
            ResponseEntity response = tokenTemplate.get('/employee?email=test@mail.com', [:])
        then:
            response.statusCode == UNAUTHORIZED
            response.body == '{"error":"unauthorized","error_description":"Full authentication is required to access this resource"}'
    }
}

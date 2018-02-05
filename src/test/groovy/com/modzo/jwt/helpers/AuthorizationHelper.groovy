package com.modzo.jwt.helpers

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

import java.nio.charset.Charset

@Component
class AuthorizationHelper {

    private static final String TOKEN_URL = '/oauth/token?grant_type=password&client_id=fooClientIdPassword'

    @Autowired
    private TestRestTemplate restTemplate

    String adminToken(){
        return getToken('admin', 'nimda')
    }

    String userToken(){
        return getToken('john', '123')
    }

    String getToken(String email, String password) {
        ResponseEntity<String> response = restTemplate.postForEntity(
                TOKEN_URL + "&username=${email}&password=${password}",
                HttpEntityBuilder.builder().basic('fooClientIdPassword', 'secret').build(),
                String
        )

        return new JsonSlurper().parseText(response.body).access_token
    }
}

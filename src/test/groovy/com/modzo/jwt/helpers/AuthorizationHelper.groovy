package com.modzo.jwt.helpers

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

import static com.modzo.jwt.helpers.HttpEntityBuilder.builder

@Component
class AuthorizationHelper {

    private static final String TOKEN_URL = '/oauth/token?'

    @Autowired
    private TestRestTemplate restTemplate

    String adminToken() {
        return getToken('admin', 'nimda').accessToken
    }

    String userToken() {
        return getToken('john', '123').accessToken
    }

    TokenResponse adminTokenEntity() {
        return getToken('admin', 'nimda')
    }

    TokenResponse getToken(String email, String password) {
        return restTemplate.postForEntity(
                TOKEN_URL + "grant_type=password" +
                        "&client_id=fooClientIdPassword" +
                        "&username=${email}" +
                        "&password=${password}",
                builder()
                        .basic('fooClientIdPassword', 'secret')
                        .build(),
                TokenResponse
        ).body
    }
}

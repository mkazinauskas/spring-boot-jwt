package com.modzo.jwt.helpers

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.stereotype.Component

import static com.modzo.jwt.helpers.HttpEntityBuilder.builder

@Component
class AuthorizationHelper {

    private static final String TOKEN_URL = '/oauth/token?'

    private final TestRestTemplate restTemplate

    AuthorizationHelper(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate
    }

    String adminAccessToken() {
        return adminToken().accessToken
    }

    TokenResponse adminToken() {
        return getToken('admin', 'nimda')
    }

    String userAccessToken() {
        return userToken().accessToken
    }

    TokenResponse userToken() {
        return getToken('john', '123')
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

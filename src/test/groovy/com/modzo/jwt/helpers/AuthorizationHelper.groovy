package com.modzo.jwt.helpers

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.stereotype.Component

import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static com.modzo.jwt.init.TestDataInit.TEST_CLIENT

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
                        "&client_id=${TEST_CLIENT.clientId}" +
                        "&username=${email}" +
                        "&password=${password}",
                builder()
                        .basic(TEST_CLIENT.clientId, TEST_CLIENT.secret)
                        .build(),
                TokenResponse
        ).body
    }
}

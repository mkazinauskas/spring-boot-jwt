package com.modzo.jwt.helpers

import com.modzo.jwt.Urls
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.stereotype.Component

import static com.modzo.jwt.domain.clients.Client.GrantType.PASSWORD
import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static com.modzo.jwt.init.TestDataInit.*

@Component
class AuthorizationHelper {
    private final TestRestTemplate restTemplate

    AuthorizationHelper(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate
    }

    String adminAccessToken() {
        return adminToken().accessToken
    }

    TokenResponse adminToken() {
        return getToken(TEST_ADMIN_USER.email, TEST_ADMIN_USER.password)
    }

    String userAccessToken() {
        return userToken().accessToken
    }

    TokenResponse userToken() {
        return getToken(TEST_USER.email, TEST_USER.password)
    }

    String registeredUserAccessToken() {
        return registeredUserToken().accessToken
    }

    TokenResponse registeredUserToken() {
        return getToken(TEST_REGISTERED_USER.email, TEST_REGISTERED_USER.password)
    }

    TokenResponse getToken(String email, String password) {
        return restTemplate.postForEntity(
                Urls.getAccessToken(PASSWORD, TEST_CLIENT.clientId, email, password),
                builder()
                        .basic(TEST_CLIENT.clientId, TEST_CLIENT.secret)
                        .build(),
                TokenResponse
        ).body
    }
}

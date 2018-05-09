package com.modzo.jwt.helpers

import com.modzo.jwt.Urls
import com.modzo.jwt.init.TestDataInit
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.stereotype.Component

import static com.modzo.jwt.domain.clients.Client.GrantType.PASSWORD
import static com.modzo.test.helpers.HttpEntityBuilder.builder

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
        return getToken(TestDataInit.TEST_ADMIN_USER.email, TestDataInit.TEST_ADMIN_USER.password)
    }

    String userAccessToken() {
        return userToken().accessToken
    }

    TokenResponse userToken() {
        return getToken(TestDataInit.TEST_USER.email, TestDataInit.TEST_USER.password)
    }

    String registeredUserAccessToken() {
        return registeredUserToken().accessToken
    }

    TokenResponse registeredUserToken() {
        return getToken(TestDataInit.TEST_REGISTERED_USER.email, TestDataInit.TEST_REGISTERED_USER.password)
    }

    TokenResponse getToken(String email, String password) {
        return restTemplate.postForEntity(
                Urls.getAccessToken(PASSWORD, TestDataInit.TEST_CLIENT.clientId, email, password),
                builder()
                        .basic(TestDataInit.TEST_CLIENT.clientId, TestDataInit.TEST_CLIENT.secret)
                        .build(),
                TokenResponse
        ).body
    }
}

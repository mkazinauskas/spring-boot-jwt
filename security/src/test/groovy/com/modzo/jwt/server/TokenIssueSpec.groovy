package com.modzo.jwt.server

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.Urls
import com.modzo.jwt.helpers.TokenResponse
import com.modzo.jwt.init.TestDataInit
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import static com.modzo.jwt.domain.clients.Client.GrantType.PASSWORD
import static com.modzo.test.helpers.HttpEntityBuilder.builder
import static org.springframework.http.HttpMethod.POST

class TokenIssueSpec extends AbstractSpec {

    void 'should create token for client'() {
        when:
            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                    Urls.getAccessToken(
                            PASSWORD,
                            TestDataInit.TEST_CLIENT.clientId,
                            TestDataInit.TEST_ADMIN_USER.email,
                            TestDataInit.TEST_ADMIN_USER.password
                    ),
                    builder()
                            .basic(TestDataInit.TEST_CLIENT.clientId, TestDataInit.TEST_CLIENT.secret)
                            .build(),
                    TokenResponse
            )
        then:
            response.statusCode == HttpStatus.OK
        and:
            TokenResponse body = response.body
            body.accessToken.length() > 0
            body.refreshToken.length() > 0
    }

    void 'should fail to create token for client if credentials are incorrect'() {
        when:
            ResponseEntity response = restTemplate.postForEntity(
                    Urls.getAccessToken(PASSWORD, TestDataInit.TEST_CLIENT.clientId, TestDataInit.TEST_ADMIN_USER.email, 'wrongPassword'),
                    builder()
                            .basic(TestDataInit.TEST_CLIENT.clientId, TestDataInit.TEST_CLIENT.secret)
                            .build(),
                    String
            )
        then:
            response.statusCode == HttpStatus.BAD_REQUEST
    }

    void 'should create use the same token for the same user'() {
        when:
            String firstAdminToken = authorizationHelper.adminAccessToken()
        and:
            String secondAdminToken = authorizationHelper.adminAccessToken()
        then:
            firstAdminToken == secondAdminToken
    }

    void 'should get access token with refresh token'() {
        given:
            TokenResponse tokenBeforeRefresh = authorizationHelper.adminToken()
        when:
            ResponseEntity<TokenResponse> refreshResponse = restTemplate.exchange(
                    Urls.refreshTokens(tokenBeforeRefresh.refreshToken),
                    POST,
                    builder()
                            .basic('test', 'secret')
                            .build(),
                    TokenResponse)
        then:
            refreshResponse.statusCode == HttpStatus.OK
        and:
            TokenResponse tokenAfterRefresh = refreshResponse.body
            tokenBeforeRefresh.organization == tokenAfterRefresh.organization
            tokenBeforeRefresh.scope == tokenAfterRefresh.scope
        and:
            tokenBeforeRefresh.refreshToken != tokenAfterRefresh.refreshToken
    }
}

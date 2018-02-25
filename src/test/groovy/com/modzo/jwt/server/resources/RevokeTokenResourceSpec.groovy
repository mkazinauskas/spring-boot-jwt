package com.modzo.jwt.server.resources

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.helpers.TokenResponse
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.provider.token.TokenStore

import static com.modzo.jwt.Urls.*
import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static com.modzo.jwt.init.TestDataInit.TEST_CLIENT
import static org.springframework.http.HttpMethod.DELETE
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.OK

class RevokeTokenResourceSpec extends AbstractSpec {

    @Autowired
    TokenStore tokenStore

    TokenResponse userToken

    void setup() {
        userToken = authorizationHelper.userToken()
    }

    def 'revoked token should not be present in token store'() {
        when:
            ResponseEntity<String> entity = restTemplate.exchange(
                    revokeToken(),
                    DELETE,
                    builder().bearer(userToken.accessToken).build(),
                    String)
        then:
            entity.statusCode == OK
        and:
            !tokenStore.readAccessToken(userToken.accessToken)
    }

    def 'cannot refresh token from revoked refresh token'() {
        when:
            ResponseEntity<String> revokeResponse = restTemplate.exchange(
                    revokeToken(),
                    DELETE,
                    builder().bearer(userToken.accessToken).build(),
                    String)
        then:
            revokeResponse.statusCode == OK
        and:
            !tokenStore.readRefreshToken(userToken.refreshToken)
            !tokenStore.readAccessToken(userToken.accessToken)
        when:
            ResponseEntity<String> refreshResponse = restTemplate.exchange(
                    refreshTokens(userToken.refreshToken),
                    POST,
                    builder()
                            .basic(TEST_CLIENT.clientId, TEST_CLIENT.secret)
                            .build(),
                    String)
        then:
            refreshResponse.statusCode == BAD_REQUEST
        and:
            def body = new JsonSlurper().parseText(refreshResponse.body)
            body.error == 'invalid_grant'
            body.error_description == "Invalid refresh token: ${userToken.refreshToken}"
    }
}
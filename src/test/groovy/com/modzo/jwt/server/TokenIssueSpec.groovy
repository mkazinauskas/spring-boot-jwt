package com.modzo.jwt.server

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.helpers.TokenResponse
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static org.springframework.http.HttpMethod.POST

class TokenIssueSpec extends AbstractSpec {

    def 'should create token for client'() {
        when:
            ResponseEntity response = restTemplate.postForEntity('/oauth/token?grant_type=password&client_id=fooClientIdPassword&username=admin&password=nimda',
                    builder()
                            .basic('fooClientIdPassword', 'secret')
                            .build(),
                    String
            )
        then:
            response.statusCode == HttpStatus.OK
        and:
            def body = new JsonSlurper().parseText(response.body)
            body.access_token.length() > 0
    }

    def 'should fail to create token for client if credentials are incorrect'() {
        when:
            ResponseEntity response = restTemplate.postForEntity('/oauth/token?grant_type=password&client_id=fooClientIdPassword&username=admin&password=wrongPassword',
                    builder()
                            .basic('fooClientIdPassword', 'secret')
                            .build(),
                    String
            )
        then:
            response.statusCode == HttpStatus.BAD_REQUEST
    }

    def 'should create use the same token for the same user'() {
        when:
            String firstAdminToken = authorizationHelper.adminToken()
        and:
            String secondAdminToken = authorizationHelper.adminToken()
        then:
            firstAdminToken == secondAdminToken
    }

    def 'should get access token with refresh token'() {
        given:
            TokenResponse tokenBeforeRefresh = authorizationHelper.adminTokenEntity()
        when:
            ResponseEntity<TokenResponse> refreshResponse = restTemplate.exchange(
                    '/oauth/token?grant_type=refresh_token' +
                            "&refresh_token=${tokenBeforeRefresh.refreshToken}",
                    POST,
                    builder()
                            .basic('fooClientIdPassword', 'secret')
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

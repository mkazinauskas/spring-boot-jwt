package com.modzo.jwt.server.resources

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.helpers.TokenResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.provider.token.TokenStore

import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static org.springframework.http.HttpMethod.DELETE
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpMethod.PUT
import static org.springframework.http.HttpStatus.*

class RevokeTokenResourceSpec extends AbstractSpec {

    @Autowired
    TokenStore tokenStore

    def 'revoked token should not be present in token store'() {
        given:
            String tokenBeforeRevoke = authorizationHelper.adminToken()
        when:
            ResponseEntity<String> entity = restTemplate.exchange('/tokens/invalidate',
                    DELETE,
                    builder()
                            .bearer(tokenBeforeRevoke)
                            .build(),
                    String)
        then:
            entity.statusCode == OK
        and:
            !tokenStore.removeAccessToken(tokenStore.readAccessToken(tokenBeforeRevoke))
    }

    //ISSUE HERE...
    def 'revoked token should not be refreshable'() {
        given:
            TokenResponse tokenBeforeRevoke = authorizationHelper.adminTokenEntity()
        when:
            ResponseEntity<String> revokeResponse = restTemplate.exchange(
                    '/tokens/invalidate',
                    DELETE,
                    builder()
                            .bearer(tokenBeforeRevoke.accessToken)
                            .build(),
                    String)
        then:
            revokeResponse.statusCode == OK
        when:
            ResponseEntity<String> refreshResponse = restTemplate.exchange(
                    '/oauth/token?grant_type=refresh_token' +
                            "&refresh_token=${tokenBeforeRevoke.refreshToken}",
                    POST,
                    builder()
                            .basic('fooClientIdPassword', 'secret')
                            .build(),
                    String)
        then:
            refreshResponse.statusCode == OK
    }
}

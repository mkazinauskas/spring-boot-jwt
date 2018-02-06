package com.modzo.jwt.server.resources

import com.modzo.jwt.AbstractSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.provider.token.TokenStore

import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static org.springframework.http.HttpMethod.DELETE

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
            entity.statusCode == HttpStatus.OK
        and:
            !tokenStore.removeAccessToken(tokenStore.readAccessToken(tokenBeforeRevoke))
    }
}

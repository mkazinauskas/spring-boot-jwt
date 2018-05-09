package com.modzo.security.resources.management.revoke;

import com.modzo.security.helpers.TokenHelper;
import com.modzo.security.server.security.TokenDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
class RevokeTokenResource {

    private final TokenStore tokenServices;

    private final TokenHelper tokenHelper;

    public RevokeTokenResource(TokenStore tokenServices, TokenHelper tokenHelper) {
        this.tokenServices = tokenServices;
        this.tokenHelper = tokenHelper;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/management/tokens")
    ResponseEntity revokeTokens() {
        TokenDetails details = tokenHelper.getDetails();
        tokenServices.removeAccessToken(
                tokenServices.readAccessToken(details.getAccessToken()
                        .orElseThrow(() -> new IllegalArgumentException("Access token is not present")))
        );

        OAuth2RefreshToken oAuth2RefreshToken = tokenServices.readRefreshToken(details.getRefreshToken()
                .orElseThrow(() -> new IllegalArgumentException("refresh token is not present")));
        tokenServices.removeRefreshToken(oAuth2RefreshToken);
        return ResponseEntity.ok().build();
    }

}
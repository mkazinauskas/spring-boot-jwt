package com.modzo.jwt.server.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class RevokeTokenResource {

    private final TokenStore tokenServices;

    public RevokeTokenResource(TokenStore tokenServices) {
        this.tokenServices = tokenServices;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/tokens", params = {"accessToken"})
    ResponseEntity revokeAccessToken(@RequestParam("accessToken") String accessToken) {
        tokenServices.removeAccessToken(tokenServices.readAccessToken(accessToken));
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/tokens", params = {"refreshToken"})
    ResponseEntity revokeRefreshToken(@RequestParam("refreshToken") String refreshToken) {
        OAuth2RefreshToken oAuth2RefreshToken = tokenServices.readRefreshToken(refreshToken);
        tokenServices.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);
        tokenServices.removeRefreshToken(oAuth2RefreshToken);
        return ResponseEntity.ok().build();
    }
}
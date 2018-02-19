package com.modzo.jwt.resources;

import com.modzo.jwt.server.security.TokenDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

@Component
public class TokenHelper {
    private final TokenStore tokenStore;

    public TokenHelper(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public TokenDetails getDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (principal instanceof OAuth2AuthenticationDetails) {
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(((OAuth2AuthenticationDetails) principal).getTokenValue());
            return new TokenDetails(accessToken.getAdditionalInformation());
        }
        throw new IllegalArgumentException("Token data is not present");
    }

}

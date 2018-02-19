package com.modzo.jwt.server.security;

import com.modzo.jwt.domain.users.Users;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

@Component
class CustomTokenEnhancer implements TokenEnhancer {

    private final Users users;

    public CustomTokenEnhancer(Users users) {
        this.users = users;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(
                new TokenDetails(users, authentication.getName()).asMap());
        return accessToken;
    }
}

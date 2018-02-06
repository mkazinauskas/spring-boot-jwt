package com.modzo.jwt.helpers

import com.fasterxml.jackson.annotation.JsonProperty

class TokenResponse {
    @JsonProperty('access_token')
    String accessToken

    @JsonProperty('token_type')
    String tokenType

    @JsonProperty('refresh_token')
    String refreshToken

    @JsonProperty('expires_in')
    String expiresIn

    String scope

    String organization

    String jti
}

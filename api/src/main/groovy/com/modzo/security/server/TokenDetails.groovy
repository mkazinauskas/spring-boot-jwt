package com.modzo.security.server

import com.modzo.domain.users.User
import com.modzo.domain.users.Users
import org.springframework.security.oauth2.common.OAuth2AccessToken

import static com.modzo.domain.DomainException.userByEmailWasNotFound
import static java.util.Optional.of

class TokenDetails {
    final String uniqueId

    final String accessToken

    final String refreshToken

    TokenDetails(Users users, String email) {
        User user = users.findByEmail(email)
                .orElseThrow { userByEmailWasNotFound() }
        uniqueId = user.uniqueId
        accessToken = null
        refreshToken = null
    }

    TokenDetails(OAuth2AccessToken accessToken) {
        uniqueId = accessToken.additionalInformation.uniqueId as String
        this.accessToken = accessToken.value
        refreshToken = accessToken.refreshToken
    }

    Optional<String> getAccessToken() {
        return of(accessToken)
    }

    Optional<String> getRefreshToken() {
        return of(refreshToken)
    }

    Map<String, Object> asMap() {
        return [uniqueId: uniqueId]
    }
}

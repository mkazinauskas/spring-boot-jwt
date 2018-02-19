package com.modzo.jwt.server.security

import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users

import static com.modzo.jwt.domain.DomainException.userByEmailWasNotFound

class TokenDetails {
    final String uniqueId

    TokenDetails(Users users, String email) {
        User user = users.findByEmail(email)
                .orElseThrow { userByEmailWasNotFound() }
        uniqueId = user.uniqueId
    }

    TokenDetails(Map<String, Object> details) {
        uniqueId = details.uniqueId as String
    }

    Map<String, Object> asMap() {
        return [
                uniqueId: uniqueId
        ]
    }
}

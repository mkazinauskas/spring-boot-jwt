package com.modzo.jwt

import com.modzo.jwt.domain.clients.Client.GrantType

class Urls {
    static String getAccessToken(GrantType grantType, String clientId, String email, String password) {
        return "/oauth/token?grant_type=${grantType.type}" +
                "&client_id=${clientId}" +
                "&username=${email}" +
                "&password=${password}"
    }
}

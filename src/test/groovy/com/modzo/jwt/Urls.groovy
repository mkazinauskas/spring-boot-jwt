package com.modzo.jwt

import com.modzo.jwt.domain.clients.Client.GrantType

import static com.modzo.jwt.domain.clients.Client.GrantType.REFRESH_TOKEN

class Urls {
    static String getAccessToken(GrantType grantType, String clientId, String email, String password) {
        return "/oauth/token?grant_type=${grantType.type}" +
                "&client_id=${clientId}" +
                "&username=${email}" +
                "&password=${password}"
    }

    static String refreshTokens(String refreshToken) {
        "/oauth/token?grant_type=${REFRESH_TOKEN.type}&refresh_token=${refreshToken}"
    }

    static String revokeAccessToken(String accessToken){
        return "/tokens?accessToken=${accessToken}"
    }

    static String revokeRefreshToken(String refreshToken){
        return "/tokens?refreshToken=${refreshToken}"
    }

    static String adminClients(){
        return '/api/admin/clients'
    }

    static String adminClient(String uniqueId) {
        return "/api/admin/clients/${uniqueId}"
    }

    static String adminUpdateClientData(String uniqueId) {
        return "/api/admin/clients/${uniqueId}"
    }
}

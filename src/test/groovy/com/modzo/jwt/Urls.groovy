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

    static String revokeToken() {
        return "/api/management/tokens"
    }

    static String revokeRefreshToken(String refreshToken) {
        return "/api/management/tokens?refreshToken=${refreshToken}"
    }

    static String adminClients() {
        return '/api/admin/clients'
    }

    static String adminClient(String uniqueId) {
        return "/api/admin/clients/${uniqueId}"
    }

    static String adminUpdateClientData(String uniqueId) {
        return "/api/admin/clients/${uniqueId}"
    }

    static String adminUpdateClientSecret(String uniqueId) {
        return "/api/admin/clients/${uniqueId}/secret"
    }

    static String adminUsers() {
        return '/api/admin/users'
    }

    static String adminUser(String user) {
        return "/api/admin/users/${user}"
    }

    static String adminUserData(String uniqueId) {
        return "/api/admin/users/${uniqueId}"
    }

    static String managementUpdatePassword() {
        return '/api/management/password'
    }

    static String swaggerUi() {
        return '/swagger-ui.html'
    }

    static String index() {
        return '/'
    }

    static String registerUser() {
        return '/api/users'
    }

    static String activateUser(String activationCode) {
        return '/api/user/activation' + (activationCode ? "?activationCode=${activationCode}" : '')
    }
}

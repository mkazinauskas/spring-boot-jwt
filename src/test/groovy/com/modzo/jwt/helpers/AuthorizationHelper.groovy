package com.modzo.jwt.helpers

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

import java.nio.charset.Charset

@Component
class AuthorizationHelper {

    private static final String TOKEN_URL = '/oauth/token?grant_type=password&client_id=fooClientIdPassword'

    @Autowired
    private TokenRestTemplate requestTemplate

    String adminToken(){
        return getToken('admin', 'nimda')
    }

    String getToken(String email, String password) {
        ResponseEntity<String> response = requestTemplate.post(
                TOKEN_URL + "&username=${email}&password=${password}",
                [:],
                getAuthorizationHeaders('fooClientIdPassword', 'secret')
        )

        return new JsonSlurper().parseText(response.body).access_token
    }

    static Map<String, String> asAuthorizationHeader(String token) {
        ['Authorization': "Bearer ${token}"]
    }

    static Map<String, String> getAuthorizationHeaders(String email, String password) {
        String auth = "${email}:${password}"

        byte[] encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(Charset.forName("US-ASCII")))

        return ['Authorization'  : "Basic ${new String(encodedAuth)}",
                'X-FORWARDED-FOR': '192.168.0.1']
    }
}

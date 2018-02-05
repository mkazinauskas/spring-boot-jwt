package com.modzo.jwt.helpers

import groovy.transform.CompileStatic
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders

import java.nio.charset.Charset

import static org.springframework.http.MediaType.APPLICATION_JSON

@CompileStatic
class HttpEntityBuilder<B> {

    private B body

    private HttpHeaders headers = new HttpHeaders()

    static HttpEntityBuilder<B> builder() {
        return new HttpEntityBuilder<B>()
    }

    def <B> HttpEntityBuilder<B> body(B body) {
        this.body = body
        return this
    }

    HttpEntityBuilder<B> jsonType() {
        headers.setContentType(APPLICATION_JSON)
        return this
    }

    HttpEntityBuilder<B> bearer(String token) {
        headers.add('Authorization', "Bearer ${token}")
        return this
    }

    HttpEntityBuilder<B> basic(String username, String password) {
        String auth = "${username}:${password}"

        String encoded = Base64.getEncoder().encodeToString(auth.getBytes(Charset.forName("US-ASCII")))

        headers.add('Authorization', "Basic ${encoded}")
        return this
    }

    HttpEntity<B> build() {
        return new HttpEntity(body, headers)
    }
}

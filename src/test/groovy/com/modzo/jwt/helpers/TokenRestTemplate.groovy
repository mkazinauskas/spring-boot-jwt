package com.modzo.jwt.helpers

import groovy.json.JsonOutput
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

import java.nio.charset.Charset

@Component
class TokenRestTemplate {

    private final TestRestTemplate restTemplate

    TokenRestTemplate(TestRestTemplate testRestTemplate) {
        this.restTemplate = testRestTemplate
    }

    ResponseEntity<String> post(String url, Map<String, String> request, Map<String, String> headers) {
        HttpHeaders requestHeaders = getDefaultHeaders()
        if (headers) {
            headers.each { key, value -> requestHeaders.add(key, value) }
        }

        HttpEntity<String> entity

        if (request) {
            entity = new HttpEntity<String>(JsonOutput.toJson(request), requestHeaders)
        } else {
            entity = new HttpEntity<String>(requestHeaders)
        }

        return restTemplate.postForEntity(url, entity, String)
    }

    ResponseEntity<String> get(String url, Map<String, String> headers) {
        HttpHeaders requestHeaders = getDefaultHeaders()
        if (headers) {
            headers.each { key, value -> requestHeaders.add(key, value) }
        }

        HttpEntity<String> entity = new HttpEntity<String>(requestHeaders)

        return restTemplate.exchange(url, HttpMethod.GET, entity, String)
    }

    private static HttpHeaders getDefaultHeaders() {
        return new HttpHeaders(
                contentType: MediaType.APPLICATION_JSON
        )
    }
}

package com.modzo.jwt.helpers

import groovy.json.JsonOutput
import groovy.transform.CompileStatic
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.stereotype.Component

@Component
@CompileStatic
class TokenRestTemplate {

    private final TestRestTemplate restTemplate

    TokenRestTemplate(TestRestTemplate testRestTemplate) {
        this.restTemplate = testRestTemplate
    }

    public <R, T> ResponseEntity<R> post(String url, T request, Map<String, String> headers, Class<R> responseType) {
        HttpHeaders requestHeaders = getDefaultHeaders()
        if (headers) {
            headers.each { key, value -> requestHeaders.add(key, value) }
        }

        HttpEntity<T> entity

        if (request) {
            entity = new HttpEntity(T, requestHeaders)
        } else {
            entity = new HttpEntity(requestHeaders)
        }

        return restTemplate.postForEntity(url, entity, responseType)
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

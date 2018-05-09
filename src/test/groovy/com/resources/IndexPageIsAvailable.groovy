package com.resources

import com.modzo.AbstractSpec
import org.springframework.http.ResponseEntity

import static com.modzo.Urls.index
import static org.springframework.http.HttpStatus.FOUND
import static org.springframework.http.HttpStatus.OK

class IndexPageIsAvailable extends AbstractSpec {

    void 'index page should redirect to swagger ui'() {
        when:
            ResponseEntity<String> redirectEndpoint = restTemplate.getForEntity(index(), String)
        then:
            redirectEndpoint.statusCode == FOUND
        when:
            ResponseEntity<String> response = restTemplate.getForEntity(redirectEndpoint.headers.getLocation(), String)
        then:
            response.statusCode == OK
            response.body.contains('<title>Swagger UI</title>')
    }
}

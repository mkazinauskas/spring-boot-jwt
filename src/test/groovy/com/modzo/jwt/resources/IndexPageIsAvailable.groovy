package com.modzo.jwt.resources

import com.modzo.jwt.AbstractSpec
import org.springframework.http.ResponseEntity

import static com.modzo.jwt.Urls.index
import static org.springframework.http.HttpStatus.OK

class IndexPageIsAvailable extends AbstractSpec {

    def 'index page should be accessible publicly'() {
        when:
            ResponseEntity<String> response = restTemplate.getForEntity(index(), String)
        then:
            response.statusCode == OK
            response.body.contains('<title>Swagger UI</title>')
    }
}

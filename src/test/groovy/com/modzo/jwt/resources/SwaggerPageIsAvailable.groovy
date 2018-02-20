package com.modzo.jwt.resources

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.helpers.HttpEntityBuilder
import org.springframework.http.ResponseEntity

import static com.modzo.jwt.Urls.swaggerUi
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

class SwaggerPageIsAvailable extends AbstractSpec {

    def 'swagger ui should be available'() {
        when:
            ResponseEntity<String> response = restTemplate.exchange(
                    swaggerUi(),
                    GET,
                    HttpEntityBuilder.builder()
                            .build(),
                    String
            )
        then:
            response.statusCode == OK
            response.body.contains('<title>Swagger UI</title>')
    }
}

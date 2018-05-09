package com.modzo.swagger

import com.modzo.AbstractSpec
import com.modzo.helpers.HttpEntityBuilder
import org.springframework.http.ResponseEntity

import static com.modzo.Urls.swaggerUi
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

class SwaggerPageIsAvailable extends AbstractSpec {

    void 'swagger ui should be available'() {
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

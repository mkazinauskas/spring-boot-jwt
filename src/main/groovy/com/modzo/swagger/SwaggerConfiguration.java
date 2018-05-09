package com.modzo.swagger;

import com.modzo.JwtApplication;
import groovy.lang.MetaClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
class SwaggerConfiguration {
    @Bean
    Docket api() {
        return new Docket(SWAGGER_2)
                .ignoredParameterTypes(MetaClass.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage(JwtApplication.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build();
    }
}

package com.modzo.jwt.configuration;

import com.modzo.jwt.JwtApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.oauth2.provider.token.AccessTokenConverter.CLIENT_ID;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
class SwaggerConfiguration {
    @Bean
    Docket api() {
        return new Docket(SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(JwtApplication.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(securityScheme()))
                .securityContexts(Arrays.asList(securityContext()));
    }

    @Bean
    public SecurityConfiguration security() {
        Map<String, Object> additionalParameters = new HashMap<>();
        additionalParameters.put("grant_type", "password");
        additionalParameters.put("username", "admin@admin.com");
        additionalParameters.put("password", "adminPassword");
        return SecurityConfigurationBuilder.builder()
                .clientId("testClient")
                .clientSecret("testSecret")
                .scopeSeparator(" ")
                .additionalQueryStringParams(additionalParameters)
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    private SecurityScheme securityScheme() {

        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(new TokenEndpoint("/oauth/token", "oauth2"))
                .tokenRequestEndpoint(
                        new TokenRequestEndpoint("/oauth/token", CLIENT_ID, "secret"))
                .build();

        SecurityScheme oauth = new OAuthBuilder()
                .name("oauth2")
                .grantTypes(Arrays.asList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
        return oauth;
    }

    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {
//                new AuthorizationScope("read", "Read operation"),
//                new AuthorizationScope("write", "Write operations")
        };
        return scopes;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(new SecurityReference("oauth2", scopes())))
                .forPaths(PathSelectors.regex("/api/.*"))

                .build();
    }
}

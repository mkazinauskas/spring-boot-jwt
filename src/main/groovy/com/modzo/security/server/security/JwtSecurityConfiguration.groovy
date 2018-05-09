package com.modzo.security.server.security

import org.hibernate.validator.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@Validated
@ConfigurationProperties('security.oauth2.authorization')
class JwtSecurityConfiguration {
    @NotBlank
    String signingKey
}

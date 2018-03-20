package com.modzo.jwt.server.security.bruteforce;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

@Component
@Validated
@ConfigurationProperties("security.brute.force")
class BruteForceProtectionConfig {
    @Min(1L)
    private int cacheExpirationTimeInSeconds = 10 * 60;

    public int getCacheExpirationTimeInSeconds() {
        return cacheExpirationTimeInSeconds;
    }

    public void setCacheExpirationTimeInSeconds(int cacheExpirationTimeInSeconds) {
        this.cacheExpirationTimeInSeconds = cacheExpirationTimeInSeconds;
    }
}

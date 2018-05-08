package com.modzo.jwt.server.security.bruteforce;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.google.common.cache.RemovalCause.EXPIRED;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
class BruteForceIPsRegister {
    private static final Logger LOG = LoggerFactory.getLogger(BruteForceIPsRegister.class);

    private final Cache<FailedEmailForIp, Integer> cache;

    public BruteForceIPsRegister() {
        this(new BruteForceProtectionConfig());
    }

    public BruteForceIPsRegister(BruteForceProtectionConfig configuration) {
        cache = CacheBuilder
                .newBuilder()
                .expireAfterWrite(configuration.getCacheExpirationTimeInSeconds(), SECONDS)
                .removalListener(BruteForceIPsRegister::removed)
                .build();
    }

    boolean isBlacklisted(String ipAddress, String email) {
        Integer failedTimes = cache.getIfPresent(new FailedEmailForIp(ipAddress, email));
        return failedTimes != null && failedTimes >= 10;
    }

    int failedCount(String ipAddress, String email) {
        Integer failedTimes = cache.getIfPresent(new FailedEmailForIp(ipAddress, email));
        return failedTimes == null ? 0 : failedTimes;
    }

    void failed(String ipAddress, String email) {
        FailedEmailForIp key = new FailedEmailForIp(ipAddress, email);
        Integer failedTimes = cache.getIfPresent(key);
        if (failedTimes == null) {
            add(key, 1);
        } else {
            add(key, failedTimes + 1);
        }
    }

    void success(String ipAddress, String email) {
        FailedEmailForIp key = new FailedEmailForIp(ipAddress, email);
        cache.invalidate(key);
    }

    private static void removed(RemovalNotification<FailedEmailForIp, Integer> removalNotification) {
        if (EXPIRED.equals(removalNotification.getCause())) {
            FailedEmailForIp key = removalNotification.getKey();
            Integer value = removalNotification.getValue();
            if (key != null && value != null) {
                LOG.info("Ip `{}` block has expired with mail `{}` failed times `{}`",
                        key.ipAddress,
                        key.ipAddress,
                        value);
            }
        }
    }

    private void add(FailedEmailForIp key, int times) {
        LOG.error("Blacklisted ip address: `{}` and email: `{}` and times: `{}`", key.ipAddress, key.email, times);
        cache.put(key, times);
    }

    private static class FailedEmailForIp {
        private final String ipAddress;
        private final String email;

        FailedEmailForIp(String ipAddress, String email) {
            this.ipAddress = ipAddress;
            this.email = email;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FailedEmailForIp that = (FailedEmailForIp) o;
            return Objects.equals(ipAddress, that.ipAddress) &&
                    Objects.equals(email, that.email);
        }

        @Override
        public int hashCode() {

            return Objects.hash(ipAddress, email);
        }
    }
}

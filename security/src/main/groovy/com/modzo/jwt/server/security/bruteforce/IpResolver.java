package com.modzo.jwt.server.security.bruteforce;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Component
class IpResolver {
    private static final Logger LOG = LoggerFactory.getLogger(IpResolver.class);

    private static final String REMOTE_IP_HEADER = "X-Forwarded-Port";

    private final HttpServletRequest httpServletRequest;

    IpResolver(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    Optional<String> resolve() {
        return Stream.of(
                resolveIt(() -> httpServletRequest.getHeader(REMOTE_IP_HEADER)),
                resolveIt(httpServletRequest::getRemoteAddr)
        ).filter(StringUtils::isNotBlank)
                .findFirst();
    }

    private String resolveIt(Supplier<String> string) {
        try {
            return string.get();
        } catch (Exception ex) {
            LOG.warn("Skipping resolving ip", ex);
            return EMPTY;
        }
    }
}

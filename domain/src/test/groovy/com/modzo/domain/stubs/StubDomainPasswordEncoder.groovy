package com.modzo.domain.stubs

import com.modzo.domain.commons.DomainPasswordEncoder
import org.springframework.stereotype.Component

@Component
class StubDomainPasswordEncoder implements DomainPasswordEncoder {
    @Override
    String encode(String rawPassword) {
        return '{chiper}' + rawPassword
    }

    @Override
    boolean matches(String rawPassword, String encodedPassword) {
        return '{chiper}' + rawPassword == encodedPassword
    }
}

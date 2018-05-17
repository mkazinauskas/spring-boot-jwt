package com.modzo.domain.stubs

import com.modzo.domain.commons.ActivationEmail
import org.springframework.stereotype.Component

@Component
class StubActivationEmail implements ActivationEmail {

    final List<Data> sentMail = []

    @Override
    void send(String email, String activationCode) {
        sentMail << new Data(email, activationCode)
    }

    class Data {
        final String email
        final String activationCode

        Data(String email, String activationCode) {
            this.email = email
            this.activationCode = activationCode
        }
    }
}

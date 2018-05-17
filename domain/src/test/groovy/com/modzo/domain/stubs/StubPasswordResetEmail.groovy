package com.modzo.domain.stubs

import com.modzo.domain.commons.PasswordResetEmail
import org.springframework.stereotype.Component

@Component
class StubPasswordResetEmail implements PasswordResetEmail {

    final List<Data> sentMail = []

    @Override
    void send(String email, String passwordResetCode) {
        sentMail << new Data(email, passwordResetCode)
    }

    class Data {
        final String email
        final String passwordResetCode

        Data(String email, String passwordResetCode) {
            this.email = email
            this.passwordResetCode = passwordResetCode
        }
    }
}

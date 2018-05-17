package com.modzo.email.commands

import com.modzo.email.AbstractSpec
import com.modzo.test.helpers.RandomDataUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage

class SendPasswordResetEmailSpec extends AbstractSpec {

    @Autowired
    SendPasswordResetEmail.Handler testTarget

    void 'should send password reset email'() {
        given:
            String email = RandomDataUtil.randomEmail()
            SendPasswordResetEmail sendPasswordResetEmail = new SendPasswordResetEmail(email, 'passwordResetCode')
        when:
            testTarget.handle(sendPasswordResetEmail)
        then:
            SimpleMailMessage message = mailSender.sentSimpleMailMessages.find { it.to.first() == email }
            message.subject == 'Password reset'
            message.text == '<h1>Hi,</h1>\n' +
                    "<p>Your password reset code is <b>passwordResetCode</b></p>" +
                    '\n\n<p>Best regards!</p>'
    }
}

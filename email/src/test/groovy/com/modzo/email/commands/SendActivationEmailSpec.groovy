package com.modzo.email.commands

import com.modzo.email.AbstractSpec
import com.modzo.test.helpers.RandomDataUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage

class SendActivationEmailSpec extends AbstractSpec {

    @Autowired
    SendActivationEmail.Handler testTarget

    void 'should send activation email'() {
        given:
            String email = RandomDataUtil.randomEmail()
            SendActivationEmail sendActivationEmail = new SendActivationEmail(email, 'activationCode')
        when:
            testTarget.handle(sendActivationEmail)
        then:
            SimpleMailMessage message = mailSender.sentSimpleMailMessages.find { it.to.first() == email }
            message.subject == 'User activation'
            message.text == '<h1>Hi,</h1>\n' +
                    "<p>Your activation code is <b>activationCode</b></p>" +
                    '\n\n<p>Best regards!</p>'
    }
}

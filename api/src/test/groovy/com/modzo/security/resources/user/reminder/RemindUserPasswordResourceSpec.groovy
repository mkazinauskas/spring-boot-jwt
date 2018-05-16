package com.modzo.security.resources.user.reminder

import com.modzo.AbstractSpec
import com.modzo.domain.users.User
import com.modzo.domain.users.Users
import com.modzo.helpers.StubJavaMailSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.mail.SimpleMailMessage

import static com.modzo.Urls.remindPassword
import static com.modzo.helpers.HttpEntityBuilder.builder
import static org.springframework.http.HttpStatus.OK

class RemindUserPasswordResourceSpec extends AbstractSpec {
    @Autowired
    Users users

    @Autowired
    StubJavaMailSender mailSender

    void 'should send password reset code'() {
        given:
            User user = userHelper.createRegisteredUser(true)
        and:
            RemindUserPasswordRequest request = new RemindUserPasswordRequest(email: user.email)
        when:
            ResponseEntity<String> response = restTemplate.postForEntity(remindPassword(),
                    builder().body(request).build(),
                    String
            )
        then:
            response.statusCode == OK
            !response.body
        and:
            User userWithReminder = users.findByEmail(request.email).get()
            userWithReminder.passwordResetCode != null
        and:
            SimpleMailMessage message = mailSender.sentSimpleMailMessages.find {
                it.to.first() == userWithReminder.email
            }
            message.subject == 'Password reset'
            message.text == '<h1>Hi,</h1>\n' +
                    "<p>Your password reset code is <b>${userWithReminder.passwordResetCode}</b></p>" +
                    '\n\n<p>Best regards!</p>'
    }
}

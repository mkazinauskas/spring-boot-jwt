package com.modzo.jwt.resources.user.reminder

import com.modzo.jwt.AbstractSpec
import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import com.modzo.jwt.helpers.StubJavaMailSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.mail.SimpleMailMessage

import static com.modzo.jwt.Urls.remindPassword
import static com.modzo.jwt.helpers.HttpEntityBuilder.builder
import static org.springframework.http.HttpStatus.OK

class RemindUserPasswordResourceSpec extends AbstractSpec {
    @Autowired
    Users users

    @Autowired
    StubJavaMailSender mailSender

    def 'should send password reset code'() {
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
            message.text == "<h1>Hi,</h1>\n<p>Your password reset code is <b>${userWithReminder.passwordResetCode}</b></p>\n\n<p>Best regards!</p>"
    }
}

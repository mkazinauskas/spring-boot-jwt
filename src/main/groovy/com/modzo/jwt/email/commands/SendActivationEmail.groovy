package com.modzo.jwt.email.commands

import com.modzo.jwt.email.EmailService
import com.modzo.jwt.email.MessageTemplatingService
import org.springframework.stereotype.Component

class SendActivationEmail {
    String userUniqueId
    String email
    String activationCode

    SendActivationEmail(String userUniqueId, String email, String activationCode) {
        this.userUniqueId = userUniqueId
        this.email = email
        this.activationCode = activationCode
    }

    @Component
    static class Handler {
        private final EmailService emailService

        private final MessageTemplatingService templatingService

        Handler(EmailService emailService, MessageTemplatingService templatingService) {
            this.emailService = emailService
            this.templatingService = templatingService
        }

        void handle(SendActivationEmail command) {
            String subject = 'User activation'
            String text = templatingService.mergeTemplateIntoString(
                    MessageTemplatingService.Template.ACTIVATION,
                    ['userUniqueId': command.userUniqueId,
                    'activationCode': command.activationCode])
            emailService.sendMessage(new EmailService.Message(command.email, subject, text))
        }
    }
}

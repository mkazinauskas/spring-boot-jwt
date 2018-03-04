package com.modzo.jwt.email.commands

import com.modzo.jwt.email.EmailService
import com.modzo.jwt.email.MessageTemplatingService
import org.springframework.stereotype.Component

class SendPasswordResetEmail {
    final String email
    final String passwordResetCode

    SendPasswordResetEmail(String email, String passwordResetCode) {
        this.email = email
        this.passwordResetCode = passwordResetCode
    }

    @Component
    static class Handler {
        private final EmailService emailService

        private final MessageTemplatingService templatingService

        Handler(EmailService emailService, MessageTemplatingService templatingService) {
            this.emailService = emailService
            this.templatingService = templatingService
        }

        void handle(SendPasswordResetEmail command) {
            String subject = 'Password reset'
            String text = templatingService.mergeTemplateIntoString(
                    MessageTemplatingService.Template.PASSWORD_RESET,
                    ['email'            : command.email,
                     'passwordResetCode': command.passwordResetCode])
            emailService.sendMessage(new EmailService.Message(command.email, subject, text))
        }
    }
}

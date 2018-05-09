package com.modzo.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
class DefaultEmailService implements EmailService {
    private final JavaMailSender emailSender;

    DefaultEmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendMessage(Message message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(message.getTo());
        email.setSubject(message.getSubject());
        email.setText(message.getText());
        emailSender.send(email);
    }
}

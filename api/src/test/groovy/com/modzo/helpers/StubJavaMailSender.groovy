package com.modzo.helpers

import groovy.transform.CompileStatic
import org.springframework.context.annotation.Primary
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessagePreparator
import org.springframework.stereotype.Component

import javax.mail.internet.MimeMessage

@Component
@Primary
@CompileStatic
class StubJavaMailSender implements JavaMailSender {

    final List<SimpleMailMessage> sentSimpleMailMessages = []

    @Override
    MimeMessage createMimeMessage() {
        return null
    }

    @Override
    MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        return null
    }

    @Override
    void send(MimeMessage mimeMessage) throws MailException {
    }

    @Override
    void send(MimeMessage... mimeMessages) throws MailException {
    }

    @Override
    void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
    }

    @Override
    void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {
    }

    @Override
    void send(SimpleMailMessage simpleMessage) throws MailException {
        this.sentSimpleMailMessages.add(simpleMessage)
    }

    @Override
    void send(SimpleMailMessage... simpleMessages) throws MailException {
    }
}

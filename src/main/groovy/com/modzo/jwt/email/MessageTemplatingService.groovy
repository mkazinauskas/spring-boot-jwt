package com.modzo.jwt.email

import com.samskivert.mustache.Mustache
import groovy.util.logging.Slf4j
import org.springframework.boot.autoconfigure.mustache.MustacheResourceTemplateLoader
import org.springframework.stereotype.Component

import static com.modzo.jwt.email.EmailException.templateParsingFailed

@Component
@Slf4j
class MessageTemplatingService {

    private final Mustache.Compiler compiler

    private final MustacheResourceTemplateLoader templateLoader

    MessageTemplatingService(Mustache.Compiler compiler, MustacheResourceTemplateLoader templateLoader) {
        this.compiler = compiler
        this.templateLoader = templateLoader
    }

    String mergeTemplateIntoString(Template templatePath, Map<String, Object> model) {
        String trimmedTemplateReference = templatePath.getPath()
        try {
            Reader template = templateLoader.getTemplate(trimmedTemplateReference)
            return compiler.compile(template).execute(model)
        } catch (Exception e) {
            log.error('Template parsing has failed', e)
            throw templateParsingFailed(templatePath.getPath())
        }
    }

    enum Template {
        ACTIVATION("email-templates/user-activation"),
        PASSWORD_RESET("email-templates/password-reset")

        private final String path

        Template(String path) {
            this.path = path
        }

        String getPath() {
            return path
        }
    }

}

package com.modzo.email

import com.modzo.test.helpers.StubJavaMailSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest
class AbstractSpec extends Specification {
    @Autowired
    StubJavaMailSender mailSender
}

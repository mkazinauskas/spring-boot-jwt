package com.modzo.jwt

import com.modzo.jwt.helpers.AuthorizationHelper
import com.modzo.jwt.helpers.ClientHelper
import com.modzo.jwt.helpers.UserHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AbstractSpec extends Specification{

    @Autowired
    protected TestRestTemplate restTemplate

    @Autowired
    protected AuthorizationHelper authorizationHelper

    @Autowired
    protected UserHelper userHelper

    @Autowired
    protected ClientHelper clientHelper
}

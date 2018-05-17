package com.modzo.domain.clients

import com.modzo.domain.AbstractSpec
import com.modzo.domain.clients.commands.CreateClient
import org.springframework.beans.factory.annotation.Autowired

import static com.modzo.test.helpers.RandomDataUtil.randomClientId
import static com.modzo.test.helpers.RandomDataUtil.randomSecret

class CreateClientSpec extends AbstractSpec {
    @Autowired
    CreateClient.Handler testTarget

    void 'should create client'() {
        given:
            CreateClient createClient = new CreateClient(randomClientId(), randomSecret())
        when:
            CreateClient.Response response = testTarget.handle(createClient)
        then:
            response.uniqueId.size() == 10
    }
}

package com.email.microservice

import com.email.microservice.controller.MailRestController
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MailServiceApplicationTests {

    @Autowired
    lateinit var mailRestController: MailRestController

    @Test
    internal fun contextLoads() {
        Assertions.assertThat(mailRestController).isNotNull
    }

}

package com.email.microservice.service

import com.email.microservice.dto.EmailDTO
import com.email.microservice.service.MailServiceInterface
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class MailServiceInterfaceImplTest @Autowired constructor(
    val mailServiceInterfaceImpl: MailServiceInterface
) {

    @Test
    @Disabled
    @FlowPreview
    internal suspend fun testAsynchronouslySendEmail() {

        // given
        val dummyEmailDTO = EmailDTO(
            sender = "testSender@mailnator.com",
            recipients = setOf("pgkavogiannis@mailnator.com"),
            mailSubject = "Mail Check",
            mailBody = "This is a check mail send to you by testSender@mailnator.com."
        )
        val (sender, recipients, mailSubject, mailBody) = dummyEmailDTO

        // when
        val sendEmailResponse: Flow<Any> = mailServiceInterfaceImpl.sendEmailAsynchronously(
            sender = sender,
            recipients = recipients,
            mailSubject = mailSubject!!,
            mailBodyText = mailBody!!
        )

        // then
        Assertions.assertNotNull(sendEmailResponse)
    }

}
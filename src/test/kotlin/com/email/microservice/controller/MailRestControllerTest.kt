package com.email.microservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.SerializationFeature
import com.email.microservice.controller.MailRestController
import com.email.microservice.dto.EmailDTO
import com.email.microservice.service.DummyService
import com.email.microservice.service.MailServiceInterface
import com.email.microservice.utils.LocaleUtil
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


private const val baseApiPath = "/api"

private fun asJsonString(dto: Any): String {
    val mapper = ObjectMapper()
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
    val ow: ObjectWriter = mapper.writer().withDefaultPrettyPrinter()
    return ow.writeValueAsString(dto)
}

@WebMvcTest(MailRestController::class)
class MailRestControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockBean
    private lateinit var localeUtil: LocaleUtil

    @MockBean
    private lateinit var dummyService: DummyService

    @MockBean
    private lateinit var mailServiceInterfaceImpl: MailServiceInterface

    @Test
    @Throws(Exception::class)
    internal fun `Active profile is not 'default'`() {

        // when
        Mockito.`when`(dummyService.getActiveProfile())
            .thenReturn("dev")

        // then
        mvc.perform(
            get("$baseApiPath/test/profile")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("dev"))
    }

    @Test
    @Throws(Exception::class)
    internal fun `API status should be 'OK'`() {
        mvc.perform(
            get("$baseApiPath/status")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(HttpStatus.OK.reasonPhrase))
    }

    @Test
    @FlowPreview
    @Throws(Exception::class)
    internal suspend fun `Should check that a dummy email is sent to recipient asynchronously`() {

        // given
        val dummyEmailDTO = EmailDTO(
            sender = "testSender@mailnator.com",
            recipients = setOf("pgkavogiannis@mailnator.com", "federick@mailnator.com"),
            mailSubject = "Mail Check",
            mailBody = "This is a check mail send to you by testSender@mailnator.com."
        )
        val (sender, recipients, mailSubject, mailBody) = dummyEmailDTO

        // when
        Mockito.`when`(
            mailServiceInterfaceImpl.sendEmailAsynchronously(
                sender = sender,
                recipients = recipients,
                mailSubject = mailSubject!!,
                mailBodyText = mailBody!!
            )
        ).thenReturn(flowOf())

        val requestJson: String = asJsonString(dummyEmailDTO)

        // then
        mvc.perform(
            post("$baseApiPath/mail")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(HttpStatus.OK.reasonPhrase))
    }

}

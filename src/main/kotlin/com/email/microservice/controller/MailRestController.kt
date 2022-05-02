package com.email.microservice.controller

import com.email.microservice.dto.EmailDTO
import com.email.microservice.service.DummyService
import com.email.microservice.service.MailServiceInterface
import com.email.microservice.utils.LocaleUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.FlowPreview
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping(
    value = ["/api"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class MailRestController @Autowired constructor(
    val mailServiceInterfaceImpl: MailServiceInterface,
    val dummyService: DummyService,
    val localeUtil: LocaleUtil
) {

    @get:Tag(name = "Status")
    @get:GetMapping(path = ["/status"])
    @get:Operation(summary = "Checks API status")
    val status: String
        get() = HttpStatus.OK.reasonPhrase

    @FlowPreview
    @Tag(name = "Mail")
    @PostMapping(path = ["/mail"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Sends an email asynchronously, without waiting response from mail server")
    suspend fun sendEmailAsynchronously(@Valid @RequestBody emailDTO: EmailDTO): String {
        val (sender, recipients, mailSubject, mailBody) = emailDTO
        mailServiceInterfaceImpl.sendEmailAsynchronously(
            sender = sender,
            recipients = recipients,
            mailSubject = mailSubject.orEmpty(),
            mailBodyText = mailBody.orEmpty()
        )
        return status
    }

    /**
     * TODO: Remove; only for test purposes
     */

    @get:Tag(name = "Test")
    @get:GetMapping(path = ["/test/profile"])
    @get:Operation(summary = "Tests the active spring profile")
    val activeProfileTest: String
        get() = dummyService.getActiveProfile()

    @FlowPreview
    @Tag(name = "Test")
    @PostMapping(path = ["/test/mail"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Tests asynchronously email sending, without waiting response from mail server")
    suspend fun asynchronouslySendEmailTest(
        @RequestHeader(name = "Accept-Language", required = false) locale: Locale?,
        @Valid @RequestBody emailDTO: EmailDTO,
    ): String {
        val (sender, recipients, mailSubject, mailBody) = emailDTO
        mailServiceInterfaceImpl.sendEmailAsynchronously(
            sender = sender,
            recipients = recipients,
            mailSubject = mailSubject?.ifBlank {
                localeUtil.getMessageForContextLocale(msgCode = "label.mail.subject.default")
            } ?: "",
            mailBodyText = mailBody?.ifBlank {
                "${localeUtil.getMessageForContextLocale("label.mail.subject.default")} $sender."
            } ?: ""
        )
        return status
    }

    @Tag(name = "Test")
    @GetMapping(path = ["/test/i18n"])
    @Operation(summary = "Tests project's internationalization based on 'Accept-Language' request header")
    fun getInternationalizationTest(@RequestHeader(name = "Accept-Language", required = false) locale: Locale?): String {
        return dummyService.getLabelForLocale()
    }

    @FlowPreview
    @Tag(name = "Test")
    @GetMapping(path = ["/test/coroutine"])
    @Operation(summary = "Tests a simple coroutine which logs into server after an amount of time")
    suspend fun getCoroutineTest(): Long = dummyService.asynchronouslyPrintMessage()

}
package com.email.microservice.utils

import com.email.microservice.utils.LocaleUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
internal class LocaleUtilTest @Autowired constructor(
    val localeUtil: LocaleUtil
) {

    private val greekLocale: Locale = Locale("gr")

    @Test
    internal fun `'getMessageForContextLocale' should return values based on default 'en_US' locale`() {
        assertEquals("Test Subject", localeUtil.getMessageForContextLocale("label.mail.subject.default"))
        assertEquals(
            "This is a check mail send to you by",
            localeUtil.getMessageForContextLocale("label.mail.body.default")
        )
    }

    @Test
    internal fun testGetMessageForLocale() {
        assertEquals(
            "Δοκιμαστικό Θέμα",
            localeUtil.getMessageForLocale(msgCode = "label.mail.subject.default", locale = greekLocale)
        )
        assertEquals(
            "Δοκιμαστικό μήνυμα από τον αποστολέα",
            localeUtil.getMessageForLocale(msgCode = "label.mail.body.default", locale = greekLocale)
        )
    }

}
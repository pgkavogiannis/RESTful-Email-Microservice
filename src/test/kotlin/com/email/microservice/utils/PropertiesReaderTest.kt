package com.email.microservice.utils

import com.email.microservice.utils.PropertiesReader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.test.context.junit.jupiter.DisabledIf

@DisabledIf(expression = "#{environment['spring.profiles.active'] != 'dev'}", loadContext = true)
internal class PropertiesReaderTest {

    private val panosProperties = PropertiesReader().properties;

    @Test
    internal fun getProperty() {
        assertEquals("true", panosProperties.getProperty("mail.smtp.auth"))
        assertEquals("smtp", panosProperties.getProperty("mail.protocol"))
        assertEquals("smtp.mailtrap.io", panosProperties.getProperty("mail.smtp.host"))
        assertEquals("mailtrap-user", panosProperties.getProperty("mail.smtp.user"))
        assertEquals("mailtrap-pass", panosProperties.getProperty("mail.smtp.pass"))
        assertEquals("2525", panosProperties.getProperty("mail.smtp.port"))
        assertEquals("false", panosProperties.getProperty("mail.smtp.starttls.enable"))
    }
}
package com.email.microservice.configuration

import com.email.microservice.configuration.ConfigurationProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.test.context.junit.jupiter.DisabledIf

@DisabledIf(expression = "#{environment['spring.profiles.active'] != 'dev'}", loadContext = true)
internal class ConfigurationProviderTest {

    private val panosProperties = ConfigurationProvider.properties;

    @Test
    internal fun `Properties should be read correctly without any exception`() {
        assertEquals("true", panosProperties.getProperty("mail.smtp.auth"))
        assertEquals("smtp", panosProperties.getProperty("mail.protocol"))
        assertEquals("smtp.mailtrap.io", panosProperties.getProperty("mail.smtp.host"))
        assertEquals("mailtrap-user", panosProperties.getProperty("mail.smtp.user"))
        assertEquals("mailtrap-pass", panosProperties.getProperty("mail.smtp.pass"))
        assertEquals("2525", panosProperties.getProperty("mail.smtp.port"))
        assertEquals("false", panosProperties.getProperty("mail.smtp.starttls.enable"))
    }

}
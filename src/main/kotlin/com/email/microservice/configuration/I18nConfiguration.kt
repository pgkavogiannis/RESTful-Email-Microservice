package com.email.microservice.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import java.nio.charset.StandardCharsets
import java.util.*


@Configuration
internal class I18nConfiguration {

    @Bean
    internal fun localeResolver(): LocaleResolver {
        val slr = AcceptHeaderLocaleResolver()
        slr.defaultLocale = Locale.US
        return slr
    }

    @Bean
    internal fun messageSource(): ResourceBundleMessageSource {
        val messageSource = ResourceBundleMessageSource()
        messageSource.setBasename("i18n/messages")
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource
    }

}
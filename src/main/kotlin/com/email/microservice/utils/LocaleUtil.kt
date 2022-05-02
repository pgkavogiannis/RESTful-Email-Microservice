package com.email.microservice.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.stereotype.Component
import java.util.*

@Component
class LocaleUtil @Autowired constructor(val messageSource: ResourceBundleMessageSource) {

    fun getMessageForContextLocale(msgCode: String, args: Array<Any>? = null): String {
        val locale: Locale = LocaleContextHolder.getLocale()
        return messageSource.getMessage(msgCode, args, locale)
    }

    fun getMessageForLocale(msgCode: String, args: Array<Any>? = null, locale: Locale): String {
        return messageSource.getMessage(msgCode, args, locale)
    }
}
package com.email.microservice.configuration

import com.email.microservice.utils.MailLogger.log
import com.email.microservice.utils.PropertiesReader
import java.util.*

internal object ConfigurationProvider {

    internal var properties: Properties = Properties()

    init {
        try {
            val reader = PropertiesReader()
            properties = reader.properties
        } catch (e: Exception) {
            log.error(e.message)
            e.printStackTrace()
        }
    }

}
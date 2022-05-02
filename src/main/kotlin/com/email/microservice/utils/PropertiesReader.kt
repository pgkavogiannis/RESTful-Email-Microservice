package com.email.microservice.utils

import org.springframework.util.ResourceUtils
import java.io.File
import java.io.FileInputStream
import java.util.*

internal class PropertiesReader(propertyFileName: String = "application.properties") {

    internal val properties: Properties = Properties()

    init {
        val file: File = ResourceUtils.getFile("classpath:$propertyFileName")
        val stream = FileInputStream(file)
        properties.load(stream)
    }

}
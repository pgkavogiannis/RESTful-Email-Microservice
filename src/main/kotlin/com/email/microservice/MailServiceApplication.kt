package com.email.microservice

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@OpenAPIDefinition
@SpringBootApplication
internal class MailServiceApplication

fun main(args: Array<String>) {
    runApplication<MailServiceApplication>(*args)
}

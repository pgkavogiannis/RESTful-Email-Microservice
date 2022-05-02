package com.email.microservice.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class EmailDTO(
    @get:NotBlank
    val sender: String,

    @get:NotEmpty
    val recipients: Set<String>,

    val mailSubject: String?,

    val mailBody: String?
)
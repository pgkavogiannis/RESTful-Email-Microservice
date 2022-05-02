package com.email.microservice.service

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
interface MailServiceInterface {

    /**
     * Sends an email from [sender] to the [recipients] with [mailSubject] as subject and [mailBodyText] as body
     */
    @FlowPreview
    suspend fun sendEmailAsynchronously(
        sender: String,
        recipients: Set<String>,
        mailSubject: String,
        mailBodyText: String
    ): Flow<Any>

}
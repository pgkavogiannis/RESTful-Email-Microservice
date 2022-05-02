package com.email.microservice.service

import com.email.microservice.configuration.ConfigurationProvider
import com.email.microservice.utils.MailLogger.log
import com.sun.mail.smtp.SMTPTransport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

@Component
class MailServiceInterfaceImpl : MailServiceInterface {

    companion object {

        private const val CONTENT_TYPE: String = "text/html; charset=UTF-8"
        private val properties: Properties = ConfigurationProvider.properties
        private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    }

    @FlowPreview
    override suspend fun sendEmailAsynchronously(
        sender: String,
        recipients: Set<String>,
        mailSubject: String,
        mailBodyText: String
    ): Flow<Any> {
        coroutineScope.launch {
            try {
                val emailResponse: String = sendEmail(
                    from = sender,
                    recipients = recipients,
                    mailSubject = mailSubject,
                    mailBodyText = mailBodyText
                )
                log.info("Email sent with response equal to: $emailResponse")
            } catch (e: MessagingException) {
                log.error("Failed to send message due to: $e")
            } catch (e: SendFailedException) {
                log.error("Failed to send message due to: $e")
            }
        }
        return listOf<Flow<Any>>().asFlow()
    }

    @Throws(MessagingException::class, SendFailedException::class)
    private suspend fun sendEmail(
        from: String,
        recipients: Set<String>,
        mailSubject: String,
        mailBodyText: String
    ): String {

        val session: Session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication = PasswordAuthentication(
                properties.getProperty("mail.smtp.user"),
                properties.getProperty("mail.smtp.pass")
            )
        })

        var addresses: Array<InternetAddress> = emptyArray()
        recipients.forEach {
            addresses += InternetAddress.parse(it, false)
        }

        val multipart: Multipart = getEmptyMultipart(mailBodyText)

        // Filling message information
        val message: Message = MimeMessage(session)
        message.setFrom(InternetAddress(from))
        message.setRecipients(Message.RecipientType.TO, addresses)
        message.subject = mailSubject
        message.setText(mailBodyText)
        message.sentDate = Date()
        message.setContent(multipart, CONTENT_TYPE)

        // Sending the message
        val transport: SMTPTransport = getSmtpTransport(properties, session)
        transport.sendMessage(message, message.allRecipients)

        // Retrieving the server response and closing the connection
        val response: String = transport.lastServerResponse
        transport.close()

        return response
    }


    @Throws(MessagingException::class, NoSuchProviderException::class)
    private fun getSmtpTransport(properties: Properties, session: Session): SMTPTransport {

        val transport: SMTPTransport = session.getTransport(
            properties.getProperty("mail.protocol")
        ) as SMTPTransport

        var portObj: String? = properties.getProperty("mail.smtp.port")
        if (portObj == null) {
            log.error("Mail port misconfiguration. Setting to 465")
            portObj = "465"
        }

        transport.connect(
            properties.getProperty("mail.smtp.host"),
            portObj.toString().toInt(),
            properties.getProperty("mail.smtp.user"),
            properties.getProperty("mail.smtp.pass")
        )

        return transport
    }

    @Throws(MessagingException::class)
    private fun getEmptyMultipart(mailBodyText: String): Multipart {
        val mimeBodyPart = MimeBodyPart()
        mimeBodyPart.setContent(mailBodyText, CONTENT_TYPE)
        val multipart: Multipart = MimeMultipart()
        multipart.addBodyPart(mimeBodyPart)
        return multipart
    }

}
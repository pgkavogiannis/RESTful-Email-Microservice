package com.email.microservice.service

import com.email.microservice.utils.LocaleUtil
import com.email.microservice.utils.MailLogger.log
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

private val coroutineScope = CoroutineScope(Dispatchers.Default)

@Service
@Deprecated("This is a dummy test class and will be removed")
class DummyService @Autowired constructor(
    environment: Environment,
    val localeUtil: LocaleUtil
) {

    private val buildProfile: String = if (environment.activeProfiles.isEmpty()) {
        environment.defaultProfiles[0]
    } else {
        environment.activeProfiles[0]
    }

    internal fun getActiveProfile(): String = buildProfile

    @FlowPreview
    internal suspend fun asynchronouslyPrintMessage(): Long {
        var delayTime: Long = 0
        coroutineScope.launch {
            delayTime += 10000L
            delay(delayTime) // non-blocking delay for 10 seconds (default time unit is ms)
            log.info("Async code finished after $delayTime ms")
        }
        return delayTime + 1
    }

    internal fun getLabelForLocale(): String {
        return localeUtil.getMessageForContextLocale("label.mail.body.default")
    }
}
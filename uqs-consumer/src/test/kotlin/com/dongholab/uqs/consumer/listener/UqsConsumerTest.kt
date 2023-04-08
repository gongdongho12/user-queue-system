package com.dongholab.uqs.consumer.listener

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import java.time.Instant
import java.util.Date

class UqsConsumerTest {
    @Disabled
    @Test
    fun testThreadPoolTaskScheduler() {
        println("Started")
        threadPoolTaskScheduler().schedule(
            { println("After 2 secs") },
            Date(Instant.now().plusSeconds(2).toEpochMilli())
        )
        Thread.sleep(3 * 1000)
    }

    private fun threadPoolTaskScheduler(): ThreadPoolTaskScheduler {
        val threadPoolTaskScheduler = ThreadPoolTaskScheduler()
        threadPoolTaskScheduler.poolSize = 100
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler-")
        threadPoolTaskScheduler.initialize()
        return threadPoolTaskScheduler
    }
}

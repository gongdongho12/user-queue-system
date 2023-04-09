package com.dongholab.uqs.consumer.listener

import com.dongholab.uqs.domain.configuration.KafkaTopics
import com.dongholab.uqs.domain.configuration.KafkaTopics.TEST_V1
import com.dongholab.uqs.domain.infrastructure.util.toJsonObject
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Date

@Component
class ProductUpdateConsumer(
    private val threadPoolTaskScheduler: ThreadPoolTaskScheduler,
) {
    private val logger = LoggerFactory.getLogger(this::class.simpleName)

    @KafkaListener(
        id = "test",
        topics = [TEST_V1],
        concurrency = "1",
        containerFactory = "dongholabKafkaListenerContainerFactory"
    )
    fun listen(records: List<ConsumerRecord<ByteArray, ByteArray>>) {
        val positions = records.joinToString(",") { "${it.topic()}-${it.partition()}[${it.offset()}]" }
        logger.info("records.size=${records.size}, positions=$positions")
        records.forEach { record ->
            try {
                val testEvent = deserialize(record)
                logger.info("[${record.offset()}] Received Message - topic = ${record.topic()}, value = $testEvent")
                consumeTest(testEvent)
            } catch (e: Throwable) {
                logger.warn("TIME-OUT) ${records.size} ${e.message} ${record.offset()}")
            }
        }
    }

    private fun deserialize(record: ConsumerRecord<ByteArray, ByteArray>): TestEvent {
        return record.value().toJsonObject(TestEvent::class.java)
    }

    fun <T> consumeTest(data: T) {
        threadPoolTaskScheduler.schedule(
            { logger.info("ConsumeTest by schedule data: $data") },
            Date(Instant.now().plusSeconds(3).toEpochMilli())
        )
    }
}

data class TestEvent(val id: Long, val message: String)

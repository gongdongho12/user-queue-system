package com.dongholab.uqs.domain.producer

import com.dongholab.uqs.domain.infrastructure.util.toJsonString
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class DefaultProducer(
    @Qualifier("defaultKafkaTemplate") private val kafkaTemplate: KafkaTemplate<String, String>,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun <T : Any> publishKafka(topic: String, key: String? = null, data: T) {
        try {
            if (key != null) {
                kafkaTemplate.send(topic, key, data.toJsonString()).whenComplete { sendResult, exception ->
                    when (exception) {
                        null -> {
                            logger.info("($topic) publish success data: ${sendResult.producerRecord.toJsonString()}")
                        }
                        else -> {
                            logger.warn("($topic) publish failure by key: $key data: ${data.toJsonString()}", exception)
                            logger.warn("($topic) requeue $data")
                        }
                    }
                }
            } else {
                kafkaTemplate.send(topic, data.toJsonString()).whenComplete { sendResult, exception ->
                    when (exception) {
                        null -> {
                            logger.info("($topic) publish success data: ${sendResult.producerRecord.toJsonString()}")
                        }
                        else -> {
                            logger.warn("($topic) publish failure data: ${data.toJsonString()}", exception)
                            logger.warn("($topic) requeue $data")
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            logger.warn("($topic) ${key?.let { "key: $it " }} flush error $data", e)
        }
    }
}

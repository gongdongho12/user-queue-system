package com.dongholab.uqs.consumer.configuration

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.ByteArrayDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@Configuration
class KafkaConfiguration {
    @Value("\${kafka.endpoint.dongholab}")
    lateinit var dongholabBrokers: String

    @Value("\${kafka.consumer.group.id}")
    lateinit var consumerGroupId: String

    @Bean
    fun dongholabKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<ByteArray, ByteArray> {
        val containerFactory = ConcurrentKafkaListenerContainerFactory<ByteArray, ByteArray>()
        containerFactory.consumerFactory = consumerFactory(dongholabBrokers, consumerGroupId)
        containerFactory.isBatchListener = true
        containerFactory.containerProperties.pollTimeout = 60_000
        return containerFactory
    }

    private fun consumerFactory(brokers: String, consumerGroupId: String): ConsumerFactory<ByteArray, ByteArray> {
        val deserializer = ByteArrayDeserializer()
        return DefaultKafkaConsumerFactory(consumerProperties(brokers, consumerGroupId), deserializer, deserializer)
    }

    private fun consumerProperties(brokers: String, consumerGroupId: String): MutableMap<String, Any> {
        return hashMapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to brokers,
            ConsumerConfig.GROUP_ID_CONFIG to consumerGroupId,
            ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG to 20_000,
            ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG to 60_000,
            ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG to 1100_000,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to true,
            ConsumerConfig.FETCH_MIN_BYTES_CONFIG to 1024 * 128,
            ConsumerConfig.FETCH_MAX_BYTES_CONFIG to 1024 * 1024 * 64,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest"
        )
    }
}

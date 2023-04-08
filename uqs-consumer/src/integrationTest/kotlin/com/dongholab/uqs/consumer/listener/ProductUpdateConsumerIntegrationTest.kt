package com.dongholab.uqs.consumer.listener

import com.dongholab.uqs.domain.configuration.KafkaTopics
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
@Transactional
class ProductUpdateConsumerIntegrationTest(
    private val productUpdateConsumer: ProductUpdateConsumer
) {
    @Test
    fun `listen 작동 테스트`() {
        val resourceText = """
            {
              "id": 1,
              "message": "HelloWorld"
            }
        """.trimIndent()
        val record = ConsumerRecord(
            KafkaTopics.TEST_V1,
            1,
            0,
            "test".toByteArray(),
            resourceText.toByteArray()
        )
        productUpdateConsumer.listen(listOf(record))
        Thread.sleep(60 * 1000)
    }
}

package com.dongholab.uqs.domain.producer

import com.dongholab.uqs.domain.configuration.DefaultKafkaConfiguration
import com.dongholab.uqs.domain.configuration.KafkaTopics.TEST_V1
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
@SpringBootTest(
    classes = [
        DefaultKafkaConfiguration::class,
        com.dongholab.uqs.domain.producer.DefaultProducer::class
    ]
)
class DefaultProducerTest {
    @Autowired
    lateinit var defaultProducer: com.dongholab.uqs.domain.producer.DefaultProducer

    @Test
    fun publishProductUpdate() {
        defaultProducer.publishKafka(
            topic = TEST_V1,
            data = com.dongholab.uqs.domain.producer.TestEvent(
                1,
                "HelloWorld"
            )
        )
    }
}

data class TestEvent(val id: Long, val message: String)

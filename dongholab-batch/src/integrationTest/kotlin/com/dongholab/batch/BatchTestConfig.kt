package com.dongholab.batch

import com.dongholab.batch.configuration.BatchDataSourceConfiguration
import org.junit.jupiter.api.Tag
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Tag("batchTest")
@SpringBatchTest
@SpringBootTest(
    classes = [
        TestBatchConfig::class,
        BatchDataSourceConfiguration::class,
        DongholabBatchApplication::class
    ]
)
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BatchTestConfig {
    protected val todayZoneDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
    protected val today = todayZoneDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    protected val yesterday = todayZoneDateTime.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    @Autowired
    lateinit var jobLauncherTestUtils: JobLauncherTestUtils
}

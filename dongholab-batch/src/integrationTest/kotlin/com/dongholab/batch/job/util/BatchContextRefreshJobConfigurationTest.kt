package com.dongholab.batch.job.util

import com.dongholab.batch.BatchTestConfig
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = ["spring.batch.job.names=batchContextRefreshJob"])
class BatchContextRefreshJobConfigurationTest(
    private val jobExplorer: JobExplorer
) : BatchTestConfig() {
    @Test
    fun run() {
        val jobName = "manualMatchedProductAssignJob"
        val jobParameters = JobParametersBuilder()
            .addString("version", System.currentTimeMillis().toString())
            .addString("jobName", jobName)
            .toJobParameters()

        val jobExecution: JobExecution = jobLauncherTestUtils.launchJob(jobParameters)

        jobExplorer.findRunningJobExecutions(jobName) shouldHaveSize 0
        jobExecution.status shouldBe BatchStatus.COMPLETED
    }
}

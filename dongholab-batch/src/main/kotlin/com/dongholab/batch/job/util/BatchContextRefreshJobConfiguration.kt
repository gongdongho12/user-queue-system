package com.dongholab.batch.job.util

import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(name = ["spring.batch.job.names"], havingValue = "batchContextRefreshJob")
class BatchContextRefreshJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val jobExplorer: JobExplorer,
    private val jobRepository: JobRepository
) {

    @Bean
    fun batchContextRefreshJob(batchContextRefreshStep: Step): Job {
        return jobBuilderFactory["batchContextRefreshJob"]
            .start(batchContextRefreshStep)
            .build()
    }

    @Bean
    @JobScope
    fun batchContextRefreshStep(
        @Value("#{jobParameters[jobName]}") jobName: String,
    ): Step {
        return stepBuilderFactory["batchContextRefreshStep"]
            .tasklet { _, _ ->
                val jobExecutions = jobExplorer.findRunningJobExecutions(jobName)
                if (jobExecutions.isEmpty()) throw RuntimeException("실행중인 job없음")
                val latestJobExecution = jobExecutions.maxByOrNull { it.jobId } ?: throw RuntimeException("실행중인 job없음")
                latestJobExecution.status = BatchStatus.FAILED
                latestJobExecution.exitStatus = ExitStatus.FAILED
                for (stepExecution in latestJobExecution.stepExecutions) {
                    if (stepExecution.status == BatchStatus.COMPLETED) continue
                    stepExecution.status = BatchStatus.FAILED
                    stepExecution.exitStatus = ExitStatus.FAILED
                    jobRepository.update(stepExecution)
                }
                jobRepository.update(latestJobExecution)
                RepeatStatus.FINISHED
            }
            .build()
    }
}

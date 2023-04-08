package com.dongholab.uqs.batch.service

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.converter.DefaultJobParametersConverter
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.Properties

@Service
class BatchUtilService(
    private val applicationArguments: ApplicationArguments
) {
    fun getJobParameters(): JobParameters {
        val jobArguments = applicationArguments.nonOptionArgs.toTypedArray()
        val properties: Properties? = StringUtils.splitArrayElementsIntoProperties(jobArguments, "=")
        return DefaultJobParametersConverter().getJobParameters(properties)
    }
}

package com.dongholab.batch

import com.dongholab.domain.DomainConfigurationLoader
import org.slf4j.LoggerFactory
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Import
import kotlin.system.exitProcess

@SpringBootApplication
@EnableBatchProcessing
@Import(DomainConfigurationLoader::class)
class DongholabBatchApplication

fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger("batch-main-logger")
    val exitCode: Int = try {
        val applicationContext = SpringApplicationBuilder(DongholabBatchApplication::class.java).run(*args)
        SpringApplication.exit(applicationContext)
    } catch (e: Exception) {
        log.warn("error message: ${e.message}")
        1
    }

    log.info("exit code: $exitCode")
    exitProcess(exitCode)
}

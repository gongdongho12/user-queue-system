package com.dongholab.uqs.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@Import(com.dongholab.uqs.domain.DomainConfigurationLoader::class)
@EnableScheduling
@ConfigurationPropertiesScan
class UqsApiApplication

fun main(args: Array<String>) {
    runApplication<UqsApiApplication>(*args)
}

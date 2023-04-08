package com.dongholab.api

import com.dongholab.domain.DomainConfigurationLoader
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@Import(DomainConfigurationLoader::class)
@EnableScheduling
@ConfigurationPropertiesScan
class DongholabApiApplication

fun main(args: Array<String>) {
    runApplication<DongholabApiApplication>(*args)
}

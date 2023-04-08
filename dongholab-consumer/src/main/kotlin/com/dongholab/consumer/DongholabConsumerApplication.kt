package com.dongholab.consumer

import com.dongholab.domain.DomainConfigurationLoader
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import java.util.UUID

@SpringBootApplication
@Import(DomainConfigurationLoader::class)
class DongholabConsumerApplication

fun main(args: Array<String>) {
    System.setProperty("serverId", "${UUID.randomUUID()}")
    runApplication<DongholabConsumerApplication>(*args)
}

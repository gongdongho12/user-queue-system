package com.dongholab.uqs.consumer

import com.dongholab.uqs.domain.DomainConfigurationLoader
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import java.util.UUID

@SpringBootApplication
@Import(DomainConfigurationLoader::class)
class UqsConsumerApplication

fun main(args: Array<String>) {
    System.setProperty("serverId", "${UUID.randomUUID()}")
    runApplication<UqsConsumerApplication>(*args)
}

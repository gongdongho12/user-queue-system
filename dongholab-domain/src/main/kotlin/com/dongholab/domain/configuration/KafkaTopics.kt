package com.dongholab.domain.configuration

import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

object KafkaTopics {
    const val TEST_V1: String = "test.v1"

    fun getAllTopics(): List<String> {
        return this::class.memberProperties.map { it.javaField?.get(this) as String }
    }
}

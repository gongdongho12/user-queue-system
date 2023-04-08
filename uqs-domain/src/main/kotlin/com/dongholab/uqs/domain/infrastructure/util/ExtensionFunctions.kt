package com.dongholab.uqs.domain.infrastructure.util

import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.InputStream
import java.io.Reader
import java.net.URL
import java.nio.ByteBuffer
import java.util.concurrent.CompletableFuture

enum class Strategy {
    SNAKE, CAMEL, DEFAULT
}

fun Any.toJsonString(strategy: Strategy = Strategy.DEFAULT): String {
    val mapper = getObjectMapper(strategy)
    return mapper.writeValueAsString(this)
}

fun <T> Any.toJsonObject(javaClass: Class<T>, strategy: Strategy = Strategy.DEFAULT): T {
    val mapper = getObjectMapper(strategy)
    return when (this) {
        is String -> mapper.readValue(this, javaClass)
        is URL -> mapper.readValue(this, javaClass)
        is Reader -> mapper.readValue(this, javaClass)
        is InputStream -> mapper.readValue(this, javaClass)
        is DataInput -> mapper.readValue(this, javaClass)
        is ByteArray -> mapper.readValue(this, javaClass)
        is ByteBuffer -> mapper.readValue(ByteArrayInputStream(this.array()), javaClass)
        else -> mapper.convertValue(this, javaClass)
    }
}

private val defaultObjectMapper by lazy { defaultObjectMapper() }
private val snakeCaseObjectMapper by lazy { snakeCaseObjectMapper() }
private val camelCaseObjectMapper by lazy { camelCaseObjectMapper() }

private fun defaultObjectMapper(): ObjectMapper {
    val jacksonObjectMapper = jacksonObjectMapper()
    return jacksonObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true)
        .findAndRegisterModules()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
}

private fun snakeCaseObjectMapper(): ObjectMapper {
    val jacksonObjectMapper = jacksonObjectMapper()
    jacksonObjectMapper.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
    return jacksonObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true)
        .findAndRegisterModules()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
}

private fun camelCaseObjectMapper(): ObjectMapper {
    val jacksonObjectMapper = jacksonObjectMapper()
    jacksonObjectMapper.propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
    return jacksonObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true)
        .findAndRegisterModules()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
}

fun getObjectMapper(strategy: Strategy): ObjectMapper {
    return when (strategy) {
        Strategy.SNAKE -> snakeCaseObjectMapper
        Strategy.CAMEL -> camelCaseObjectMapper
        Strategy.DEFAULT -> defaultObjectMapper
    }
}

fun <T> List<CompletableFuture<T>>.waitForAllComputed(): List<T> {
    return this.map { it.join() }
}

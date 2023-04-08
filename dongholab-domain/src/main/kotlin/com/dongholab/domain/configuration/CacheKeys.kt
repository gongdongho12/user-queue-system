package com.dongholab.domain.configuration

import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

object CacheKeys {
    fun getAllKeys(): List<String> {
        return this::class.memberProperties.map { it.javaField?.get(this) as String }
    }
}

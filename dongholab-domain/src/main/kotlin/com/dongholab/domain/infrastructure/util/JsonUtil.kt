package com.dongholab.domain.infrastructure.util

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder

fun createGson(
    fieldNamingPolicy: FieldNamingPolicy = FieldNamingPolicy.IDENTITY,
    appendBuilderFunction: (GsonBuilder.() -> GsonBuilder)? = null
): Gson {
    return GsonBuilder()
        .setFieldNamingPolicy(fieldNamingPolicy)
        .disableHtmlEscaping()
        .apply {
            appendBuilderFunction?.let {
                apply { it() }
            }
        }
        .serializeNulls()
        .create()
}

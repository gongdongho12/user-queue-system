package com.dongholab.uqs.batch.util

import com.google.gson.Gson
import com.google.gson.JsonObject

private val gson = Gson()

fun String.toJsonObject(): JsonObject {
    return gson.fromJson(this, JsonObject::class.java)
}

fun List<String>.queryJoinging(): String? {
    return filterNot { it.isNullOrEmpty() }
        .joinToString(" and ")
        ?.takeIf { it.isNotBlank() }
}

package com.dongholab.api

import org.springframework.core.io.ClassPathResource
import java.io.BufferedReader

fun getQuery(path: String): String {
    val resource = ClassPathResource(path)
    return BufferedReader(resource.inputStream.reader()).readText()
}

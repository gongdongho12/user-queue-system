package com.dongholab.api.controller

import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController(private val env: Environment) {
    @GetMapping("/hello")
    fun hello(): Map<String, Any> {
        return helloResult()
    }

    @GetMapping("/eks-hello")
    fun helloEks(): Map<String, Any> {
        return helloResult()
    }

    private fun helloResult(): Map<String, Any> {
        return mapOf(
            "id" to System.getProperty("serverId"),
            "status" to "ok",
            "profile" to env.activeProfiles,
        )
    }
}

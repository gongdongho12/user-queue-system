package com.dongholab.uqs.api.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "dongholab")
data class DongholabComponent @ConstructorBinding constructor(
    val author: String,
    val bookmark: Map<String, String>
)

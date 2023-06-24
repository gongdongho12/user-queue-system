package com.dongholab.uqs.api.controller.dto

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String
)

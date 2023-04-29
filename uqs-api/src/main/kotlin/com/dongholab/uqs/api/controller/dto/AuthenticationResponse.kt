package com.dongholab.uqs.api.controller.dto

data class AuthenticationResponse(
    private val accessToken: String,
    private val refreshToken: String
)

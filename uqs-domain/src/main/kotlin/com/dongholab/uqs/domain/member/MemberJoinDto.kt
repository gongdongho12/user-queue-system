package com.dongholab.uqs.domain.member

data class MemberJoinDto(
    val userId: String,
    val password: String,
    val role: MemberRole = MemberRole.USER
)

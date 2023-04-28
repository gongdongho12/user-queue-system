package com.dongholab.uqs.api.controller

import com.dongholab.uqs.api.security.service.RegisterMemberService
import com.dongholab.uqs.domain.member.MemberJoinDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthorizationController(
    private val registerMemberService: RegisterMemberService
) {
    @PostMapping("/join")
    fun join(@RequestBody memberJoin: MemberJoinDto): ResponseEntity<String> {
        return try {
            registerMemberService.join(memberJoin.userId, memberJoin.password)
            ResponseEntity.ok("join success")
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
}

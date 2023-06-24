package com.dongholab.uqs.api.controller

import com.dongholab.uqs.api.controller.dto.AuthenticationResponse
import com.dongholab.uqs.api.security.service.MemberAuthService
import com.dongholab.uqs.domain.member.MemberJoinDto
import com.dongholab.uqs.domain.member.MemberLoginDto
import com.dongholab.uqs.domain.token.Token
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException


@RestController
@RequestMapping("/auth")
class AuthorizationController(
    private val memberAuthService: MemberAuthService
) {
    @PostMapping("/join")
    fun join(@RequestBody memberJoin: MemberJoinDto): ResponseEntity<String> {
        return try {
            memberAuthService.join(memberJoin)
            ResponseEntity.ok("join success")
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("/authenticate")
    fun authenticate(
        @RequestBody memberLogin: MemberLoginDto
    ): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(memberAuthService.authenticate(memberLogin))
    }

    @PostMapping("/refresh-token")
    @Throws(IOException::class)
    fun refreshToken(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<Token> {
        return ResponseEntity.ok(memberAuthService.refreshToken(request, response))
    }
}

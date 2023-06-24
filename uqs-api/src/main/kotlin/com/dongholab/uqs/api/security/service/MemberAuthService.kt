package com.dongholab.uqs.api.security.service

import com.dongholab.uqs.api.controller.dto.AuthenticationResponse
import com.dongholab.uqs.api.security.model.MemberUserDetails
import com.dongholab.uqs.domain.member.Member
import com.dongholab.uqs.domain.member.MemberJoinDto
import com.dongholab.uqs.domain.member.MemberLoginDto
import com.dongholab.uqs.domain.member.repository.mysql.MemberJpaRepository
import com.dongholab.uqs.domain.token.Token
import com.dongholab.uqs.domain.token.TokenType
import com.dongholab.uqs.domain.token.repository.mysql.TokenJpaRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class MemberAuthService(
    private val passwordEncoder: PasswordEncoder,
    private val memberRepository: MemberJpaRepository,
    private val tokenRepository: TokenJpaRepository,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) : LogoutHandler, LogoutSuccessHandler {
    fun join(memberJoin: MemberJoinDto): Long {
        val member = Member(
            userId = memberJoin.userId,
            password = passwordEncoder.encode(memberJoin.password),
            role = memberJoin.role
        )
        validateDuplicateMember(member)
        memberRepository.save(member)
        return member.id!!
    }

    private fun validateDuplicateMember(member: Member) {
        memberRepository.findByUserId(member.userId)?.let {
            throw IllegalStateException("이미 존재하는 아이디 입니다")
        }
    }

    fun authenticate(memberLogin: MemberLoginDto): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                memberLogin.userId,
                memberLogin.password
            )
        )
        val member = memberRepository.findByUserId(memberLogin.userId)
            ?: throw throw IllegalStateException("아이디가 존재하지 않습니다")
        val userDetails = MemberUserDetails(member)
        val jwtToken: String = jwtService.generateToken(userDetails)
        val refreshToken: String = jwtService.generateRefreshToken(userDetails)
        revokeAllUserTokens(member)
        saveUserToken(member, jwtToken)
        return AuthenticationResponse(jwtToken, refreshToken)
    }

    private fun saveUserToken(member: Member, jwtToken: String): Token {
        val token = Token(
            member = member,
            token = jwtToken,
            tokenType = TokenType.BEARER,
            expired = false,
            revoked = false
        )
        return tokenRepository.save(token)
    }

    private fun revokeAllUserTokens(member: Member) {
        val validUserTokens: List<Token> = tokenRepository.findAllValidTokenByMember(member.userId)
        if (validUserTokens.isEmpty()) return
        tokenRepository.saveAll(validUserTokens.map { token ->
            token.expired = true
            token.revoked = true
            token
        })
    }

    fun refreshToken(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Token {
        val tokenPrefix = TokenType.BEARER.prefix.let { "$it " }
        val authHeader: String? = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authHeader == null || !authHeader.startsWith(tokenPrefix)) {
            throw IllegalStateException("헤더정보가 정확하지 않습니다")
        }
        val refreshToken = authHeader.substring(tokenPrefix.length)
        jwtService.extractUsername(refreshToken).let { userId ->
            memberRepository.findByUserId(userId)?.let { member ->
                val userDetails = MemberUserDetails(member)
                if (jwtService.isTokenValid(refreshToken, userDetails)) {
                    val accessToken: String = jwtService.generateToken(userDetails)
                    revokeAllUserTokens(member)
                    return saveUserToken(member, accessToken)
                }
            }
        }
        throw IOException("토큰 정보가 존재하지 않습니다")
    }

    override fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val tokenPrefix = TokenType.BEARER.prefix.let { "$it " }
        val authHeader: String? = request.getHeader(HttpHeaders.AUTHORIZATION)
        authHeader?.takeIf { it.startsWith(tokenPrefix) }?.let {
            val jwtToken = authHeader.substring(tokenPrefix.length)
            tokenRepository.findByToken(jwtToken)?.let { storedToken ->
                storedToken.expired = true
                storedToken.revoked = true
                tokenRepository.save(storedToken)
                SecurityContextHolder.clearContext()
            }
        }
    }

    override fun onLogoutSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        SecurityContextHolder.clearContext()
    }
}

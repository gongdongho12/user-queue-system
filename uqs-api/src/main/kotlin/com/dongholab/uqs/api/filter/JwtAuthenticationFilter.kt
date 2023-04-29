package com.dongholab.uqs.api.filter

import com.dongholab.uqs.api.security.service.JwtService
import com.dongholab.uqs.domain.token.TokenType
import com.dongholab.uqs.domain.token.repository.TokenRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException
import org.apache.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailService: UserDetailsService,
    private val tokenRepository: TokenRepository
) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val tokenPrefix = TokenType.BEARER.prefix.let { "$it " }
        val authHeader: String? = request.getHeader(HttpHeaders.AUTHORIZATION)
        when {
            request.servletPath.contains("/auth") -> filterChain.doFilter(request, response)
            authHeader == null || !authHeader.startsWith(tokenPrefix) -> filterChain.doFilter(request, response)
            else -> {
                val jwtToken: String = authHeader.substring(tokenPrefix.length)
                if (SecurityContextHolder.getContext().authentication == null) {
                    jwtService.extractUsername(jwtToken).let { userId ->
                        val userDetails = userDetailService.loadUserByUsername(userId)
                        val isTokenValid: Boolean =
                            tokenRepository.findByToken(jwtToken)?.let { token -> !token.expired && !token.revoked }
                                ?: false
                        if (jwtService.isTokenValid(jwtToken, userDetails) && isTokenValid) {
                            val authToken = UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.authorities
                            )
                            authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                            SecurityContextHolder.getContext().authentication = authToken
                        }
                    }
                }
                filterChain.doFilter(request, response)
            }
        }
    }
}

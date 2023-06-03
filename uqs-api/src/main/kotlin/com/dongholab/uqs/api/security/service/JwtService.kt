package com.dongholab.uqs.api.security.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.Date


@Service
class JwtService(
    @Value("\${jwt.secretkey}")
    private val secretKey: String,
    @Value("\${jwt.expiration}")
    private val jwtExpiration: Long,
    @Value("\${jwt.refreshtoken.expiration}")
    private val refreshExpiration: Long
) {
    fun extractUsername(token: String): String {
        return extractClaim(token) { subject }
    }

    fun <T> extractClaim(token: String, claimsResolver: Claims.() -> T): T {
        val claims = extractAllClaims(token)
        return claims.claimsResolver()
    }

    fun generateToken(userDetails: UserDetails): String {
        return generateToken(mapOf(), userDetails)
    }

    fun generateToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails
    ): String {
        return buildToken(extraClaims, userDetails, jwtExpiration)
    }

    fun generateRefreshToken(
        userDetails: UserDetails
    ): String {
        return buildToken(HashMap(), userDetails, refreshExpiration)
    }

    private fun buildToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
        expiration: Long
    ): String {
        val currentDateTimeMills = System.currentTimeMillis()
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(currentDateTimeMills))
            .setExpiration(Date(currentDateTimeMills + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token) { expiration }
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
    }

    private fun getSignInKey(): Key {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}

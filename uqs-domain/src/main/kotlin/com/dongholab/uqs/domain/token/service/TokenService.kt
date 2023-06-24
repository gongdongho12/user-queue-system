package com.dongholab.uqs.domain.token.service

import com.dongholab.uqs.domain.token.Token
import com.dongholab.uqs.domain.token.repository.mysql.TokenJpaRepository
import org.springframework.stereotype.Service

@Service
class TokenService(private val tokenJpaRepository: TokenJpaRepository) {
    fun findAllValidTokenByMember(userId: String): List<Token> {
        return tokenJpaRepository.findAllValidTokenByMember(userId)
    }

    fun findByToken(token: String): Token? {
        return tokenJpaRepository.findByToken(token)
    }
}

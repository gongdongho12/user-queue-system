package com.dongholab.uqs.domain.token.service

import com.dongholab.uqs.domain.token.Token
import com.dongholab.uqs.domain.token.repository.mysql.TokenJpaRepository
import org.springframework.stereotype.Service

@Service
class TokenService(private val tokenJpaRepository: TokenJpaRepository) {
    fun findAllValidTokenByMember(id: Long): List<Token> {
        return tokenJpaRepository.findAllValidTokenByMember(id)
    }

    fun findByToken(token: String): Token? {
        return tokenJpaRepository.findByToken(token)
    }
}

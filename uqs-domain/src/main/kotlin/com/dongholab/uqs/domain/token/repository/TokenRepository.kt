package com.dongholab.uqs.domain.token.repository

import com.dongholab.uqs.domain.token.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TokenRepository : JpaRepository<Token, Long> {
    @Query("select t from Token t inner join Member m on t.member.id = m.id where m.id = :id and (t.expired = false or t.revoked = false)")
    fun findAllValidTokenByMember(id: Long): List<Token>

    fun findByToken(token: String): Token?
}

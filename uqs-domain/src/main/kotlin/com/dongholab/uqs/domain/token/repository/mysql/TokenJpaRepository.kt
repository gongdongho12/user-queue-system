package com.dongholab.uqs.domain.token.repository.mysql

import com.dongholab.uqs.domain.token.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TokenJpaRepository : JpaRepository<Token, Long> {
    @Query("select t from Token t inner join Member m on t.member.userId = m.userId where m.userId = :userId and (t.expired = false or t.revoked = false)")
    fun findAllValidTokenByMember(userId: String): List<Token>

    fun findByToken(token: String): Token?
}

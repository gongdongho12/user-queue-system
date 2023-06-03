package com.dongholab.uqs.domain.member.repository.mysql

import com.dongholab.uqs.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<Member, Long> {
    fun findByUserId(userId: String): Member?
}

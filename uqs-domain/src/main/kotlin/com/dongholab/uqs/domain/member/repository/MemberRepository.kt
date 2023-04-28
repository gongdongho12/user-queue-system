package com.dongholab.uqs.domain.member.repository

import com.dongholab.uqs.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository: JpaRepository<Member, Long> {
    fun findByUserid(userId: String): Member?
}

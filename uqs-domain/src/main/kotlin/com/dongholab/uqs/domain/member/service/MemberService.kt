package com.dongholab.uqs.domain.member.service

import com.dongholab.uqs.domain.member.Member
import com.dongholab.uqs.domain.member.repository.mysql.MemberJpaRepository
import org.springframework.stereotype.Service

@Service
class MemberService(private val memberJpaRepository: MemberJpaRepository) {
    fun findByUserid(userId: String): Member? {
        return memberJpaRepository.findByUserid(userId)
    }
}

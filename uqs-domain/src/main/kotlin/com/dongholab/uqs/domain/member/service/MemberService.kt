package com.dongholab.uqs.domain.member.service

import com.dongholab.uqs.domain.member.Member
import com.dongholab.uqs.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service


@Service
class MemberService(private val repository: MemberRepository) {
    fun findByUserid(userId: String): Member? {
        return repository.findByUserid(userId)
    }
}

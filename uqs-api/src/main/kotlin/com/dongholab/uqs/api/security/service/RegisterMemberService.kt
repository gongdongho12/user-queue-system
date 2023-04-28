package com.dongholab.uqs.api.security.service

import com.dongholab.uqs.domain.member.Member
import com.dongholab.uqs.domain.member.repository.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class RegisterMemberService(
    private val passwordEncoder: PasswordEncoder,
    private val memberRepository: MemberRepository
) {
    fun join(userId: String, password: String): Long {
        val member = Member(userId = userId, password = passwordEncoder.encode(password), role = "USER")
        validateDuplicateMember(member)
        memberRepository.save(member)
        return member.id!!
    }

    private fun validateDuplicateMember(member: Member) {
        memberRepository.findByUserid(member.userId).let {
            throw IllegalStateException("이미 존재하는 아이디 입니다")
        }
    }
}

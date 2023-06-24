package com.dongholab.uqs.api.security

import com.dongholab.uqs.api.security.model.MemberUserDetails
import com.dongholab.uqs.domain.member.Member
import com.dongholab.uqs.domain.member.service.MemberService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component


@Component
class UqsUserDetailService(private val memberService: MemberService) : UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(userId: String): UserDetails {
        val member: Member = memberService.findByUserid(userId)
            ?: throw UsernameNotFoundException("아이디 ${userId}은(는) 없는 회원입니다")
        return MemberUserDetails(member)
    }
}

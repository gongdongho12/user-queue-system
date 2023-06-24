package com.dongholab.uqs.api.security.model

import com.dongholab.uqs.domain.member.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class MemberUserDetails(private val member: Member) : UserDetails {
    override fun getAuthorities(): Collection<out GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_${member.role.name}"))
    }

    override fun getPassword(): String {
        return member.password
    }

    override fun getUsername(): String {
        return member.userId
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    fun getId() = member.id
}

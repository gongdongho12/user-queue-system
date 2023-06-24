package com.dongholab.uqs.domain.member

import com.dongholab.uqs.domain.model.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "members")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(unique = true)
    val userId: String,
    val password: String,
    @Enumerated(EnumType.STRING)
    val role: MemberRole = MemberRole.USER
) : BaseTimeEntity()

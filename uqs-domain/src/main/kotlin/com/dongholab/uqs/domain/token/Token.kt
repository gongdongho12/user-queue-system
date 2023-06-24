package com.dongholab.uqs.domain.token

import com.dongholab.uqs.domain.member.Member
import com.dongholab.uqs.domain.model.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "tokens")
class Token(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(unique = true)
    val token: String,
    @Enumerated(EnumType.STRING)
    val tokenType: TokenType = TokenType.BEARER,
    var revoked: Boolean,
    var expired: Boolean,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val member: Member
) : BaseTimeEntity()

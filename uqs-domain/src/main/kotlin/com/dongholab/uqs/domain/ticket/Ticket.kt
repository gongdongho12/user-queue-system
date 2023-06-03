package com.dongholab.uqs.domain.ticket

import com.dongholab.uqs.domain.model.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Ticket(
    @Column(nullable = false) var title: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L
) : BaseTimeEntity()

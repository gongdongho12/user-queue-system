package com.dongholab.uqs.domain.ticket

import com.dongholab.uqs.domain.model.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "tickets")
class Ticket(
    @Column(nullable = false) var key: String,
    @Column(nullable = false) var data: String,
    @Column(nullable = false) val userId: Long,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L
) : BaseTimeEntity()

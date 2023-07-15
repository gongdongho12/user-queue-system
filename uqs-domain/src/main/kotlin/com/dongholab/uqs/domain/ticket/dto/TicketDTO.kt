package com.dongholab.uqs.domain.ticket.dto

import com.dongholab.uqs.domain.ticket.Ticket

data class TicketDTO(
    val userId: Long,
    val key: String,
    val data: String
) {
    fun toDAO(): Ticket {
        return Ticket(key = key, data = data, userId = userId)
    }
}

fun Ticket.toDTO(): TicketDTO {
    return TicketDTO(userId, key, data)
}

package com.dongholab.uqs.domain.ticket.service

import com.dongholab.uqs.domain.ticket.Ticket
import com.dongholab.uqs.domain.ticket.repository.mysql.TicketJpaRepository
import org.springframework.stereotype.Service

@Service
class TicketService(private val ticketRepository: TicketJpaRepository) {
    fun saveTicket(value: String): Ticket {
        return ticketRepository.save(Ticket(value))
    }
}

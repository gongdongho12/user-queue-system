package com.dongholab.uqs.domain.ticket.repository

import com.dongholab.uqs.domain.ticket.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketJpaRepository : JpaRepository<Ticket, Long> {
}

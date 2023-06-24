package com.dongholab.uqs.domain.ticket.repository.mysql

import com.dongholab.uqs.domain.ticket.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketJpaRepository : JpaRepository<Ticket, Long> {
}

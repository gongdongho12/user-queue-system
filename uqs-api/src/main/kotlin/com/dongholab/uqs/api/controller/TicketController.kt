package com.dongholab.uqs.api.controller

import com.dongholab.uqs.domain.ticket.Ticket
import com.dongholab.uqs.domain.ticket.service.TicketService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TicketController(private val ticketService: TicketService) {
    @GetMapping("/save")
    fun save(): Ticket {
        return ticketService.saveTicket()
    }
}

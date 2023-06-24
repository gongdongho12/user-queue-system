package com.dongholab.uqs.api.controller

import com.dongholab.uqs.domain.ticket.Ticket
import com.dongholab.uqs.domain.ticket.service.TicketService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ticket")
class TicketController(
    private val ticketService: TicketService,
) {
    @GetMapping("/save")
    fun save(): ResponseEntity<Ticket> {
        return ResponseEntity.ok(ticketService.saveTicket())
    }
}

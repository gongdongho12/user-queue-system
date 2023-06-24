package com.dongholab.uqs.api.controller

import com.dongholab.uqs.api.security.model.MemberUserDetails
import com.dongholab.uqs.domain.configuration.KafkaTopics
import com.dongholab.uqs.domain.producer.DefaultProducer
import com.dongholab.uqs.domain.ticket.Ticket
import com.dongholab.uqs.domain.ticket.service.TicketService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ticket")
class TicketController(
    private val ticketService: TicketService,
    private val defaultProducer: DefaultProducer
) {
    @GetMapping("/save")
    fun save(authentication: Authentication): ResponseEntity<Ticket> {
        val userDetails = authentication.principal as MemberUserDetails
        val ticketMessage = "HelloWorld"
        val ticket = ticketService.saveTicket(ticketMessage)
        defaultProducer.publishKafka(
            topic = KafkaTopics.TEST_V1,
            data = TestEvent(
                userDetails.getId()!!,
                ticketMessage
            )
        )
        return ResponseEntity.ok(ticket)
    }
}

data class TestEvent(val id: Long, val message: String)

package com.dongholab.uqs.api.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException

@RestController
@RequestMapping("/user")
class UserController(
    private val userDetailsService: UserDetailsService
) {
    @GetMapping
    fun getUser(authentication: Authentication): ResponseEntity<UserDetails> {
        return try {
            val userDetails = authentication.principal as UserDetails
            val user = userDetailsService.loadUserByUsername(userDetails.username)
            ResponseEntity.ok(user)
        } catch (e: Exception) {
            throw IOException(e.message)
        }
    }
}

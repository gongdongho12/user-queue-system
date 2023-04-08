package com.dongholab.uqs.api.controller

import com.dongholab.uqs.domain.CustomClientException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(this::class.simpleName)

    @ExceptionHandler(value = [(IllegalArgumentException::class)])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleClientException(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        var errorMessage = ErrorsResponse(HttpStatus.BAD_REQUEST.toString(), ex.message ?: "")
        log.error("errorMessage -> $errorMessage")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage)
    }

    // api body params error
    @ExceptionHandler(value = [(MissingKotlinParameterException::class)])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMissingKotlinParameterException(
        ex: MissingKotlinParameterException,
        request: WebRequest
    ): ResponseEntity<Any> {
        val exceptionMessage = getParameterExceptionMessage(ex)
        var errorMessage = ErrorsResponse(HttpStatus.BAD_REQUEST.toString(), exceptionMessage ?: "")
        log.error("errorMessage -> $errorMessage")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage)
    }

    private fun getParameterExceptionMessage(ex: MissingKotlinParameterException): String {
        val name = ex.path.fold("") { jsonPath, ref ->
            val suffix = when {
                ref.index > -1 -> "[${ref.index}]"
                else -> ".${ref.fieldName}"
            }
            (jsonPath + suffix).removePrefix(".")
        }
        return "$name is required"
    }

    @ExceptionHandler(value = [(com.dongholab.uqs.domain.CustomClientException::class)])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleCustomClientException(ex: com.dongholab.uqs.domain.CustomClientException, request: WebRequest): ResponseEntity<Any> {
        var errorMessage = ErrorsResponse(ex.errorCode.toString(), ex.message ?: "")
        log.error("errorMessage -> $errorMessage")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage)
    }
}

data class ErrorsResponse(val status: String, val message: String)

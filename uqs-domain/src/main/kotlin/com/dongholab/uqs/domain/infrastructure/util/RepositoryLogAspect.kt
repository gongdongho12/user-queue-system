package com.dongholab.uqs.domain.infrastructure.util

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class RepositoryLogAspect {
    private val logger = LoggerFactory.getLogger(this::class.simpleName)

    @AfterThrowing("execution(* com.dongholab.uqs.domain..repository..*Repository.*(..))", throwing = "ex")
    fun doThrowing(joinPoint: JoinPoint, ex: Exception) {
        logger.error("[Repository Exception] signature= ${joinPoint.signature}, args= ${joinPoint.args.toList()}, message= $ex")
    }
}

package com.dongholab.uqs.domain.infrastructure.util

import com.dongholab.uqs.domain.configuration.CustomRedisTemplate
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.util.concurrent.TimeUnit

@Aspect
@Order(-1)
@Component
class RedisCacheAspect(
    private val redisTemplate: CustomRedisTemplate<String, Any?>,
) {

    @Around("@annotation(RedisCacheable)")
    fun cacheableProcess(joinPoint: ProceedingJoinPoint): Any? {
        val redisCacheable = (joinPoint.signature as MethodSignature).method.getAnnotation(RedisCacheable::class.java)
        val cacheKey = generateKey(redisCacheable.key, joinPoint, redisCacheable.hasClassAndMethodNamePrefix)
        val cacheTTL = redisCacheable.expireSecond
        if (redisTemplate.hasKey(cacheKey)) {
            val result = redisTemplate.opsForValue().get(cacheKey)
            if (result != null) return result
        }
        val methodReturnValue = joinPoint.proceed()
        if (cacheTTL < 0) {
            redisTemplate.opsForValue().set(cacheKey, methodReturnValue)
        } else {
            redisTemplate.opsForValue().set(cacheKey, methodReturnValue, cacheTTL, TimeUnit.SECONDS)
        }
        return methodReturnValue
    }

    @Around("@annotation(RedisCacheEvict)")
    fun cacheEvictProcess(joinPoint: ProceedingJoinPoint): Any? {
        val methodReturnValue = joinPoint.proceed()
        val redisCacheEvict = (joinPoint.signature as MethodSignature).method.getAnnotation(RedisCacheEvict::class.java)
        val cacheKey = generateKey(redisCacheEvict.key, joinPoint, redisCacheEvict.hasClassAndMethodNamePrefix)
        redisTemplate.delete(cacheKey)
        return methodReturnValue
    }

    @Around("@annotation(RedisCachePut)")
    fun cachePutProcess(joinPoint: ProceedingJoinPoint): Any? {
        val redisCachePut = (joinPoint.signature as MethodSignature).method.getAnnotation(RedisCachePut::class.java)
        val cacheKey = generateKey(redisCachePut.key, joinPoint, redisCachePut.hasClassAndMethodNamePrefix)
        val cacheTTL = redisCachePut.expireSecond
        val methodReturnValue = joinPoint.proceed()
        if (cacheTTL < 0) {
            redisTemplate.opsForValue().set(cacheKey, methodReturnValue)
        } else {
            redisTemplate.opsForValue().set(cacheKey, methodReturnValue, cacheTTL, TimeUnit.SECONDS)
        }
        return methodReturnValue
    }

    private fun generateKey(
        cacheKey: String,
        joinPoint: ProceedingJoinPoint,
        hasClassAndMethodNamePrefix: Boolean
    ): String {
        val generatedKey = StringUtils.arrayToCommaDelimitedString(joinPoint.args)
        if (hasClassAndMethodNamePrefix) {
            val target = joinPoint.target::class.simpleName
            val method = (joinPoint.signature as MethodSignature).method.name
            return "$cacheKey::$target.$method($generatedKey)"
        }
        return "$cacheKey::($generatedKey)"
    }
}

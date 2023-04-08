package com.dongholab.uqs.domain.configuration

import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.data.redis.serializer.SerializationException

class CustomRedisTemplate<K, V> : RedisTemplate<K, V>() {

    @Override
    override fun opsForValue(): ValueOperations<K, V> {
        return CustomValueOps(super.opsForValue(), this)
    }

    class CustomValueOps<K, V>(
        private val valueOps: ValueOperations<K, V>,
        private val restTemplate: RedisTemplate<K, V>
    ) : ValueOperations<K, V> by valueOps {
        private val log = LoggerFactory.getLogger(this::class.simpleName)

        @Override
        override fun get(key: Any): V? {
            return try {
                valueOps.get(key)
            } catch (e: SerializationException) {
                log.warn("key: $key error message: ${e.message}")
                key as K
                restTemplate.delete(key)
                null
            }
        }
    }
}

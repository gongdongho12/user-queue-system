package com.dongholab.uqs.domain.configuration

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.serializer.SerializationException
import java.util.concurrent.Callable

class CustomRedisCacheManager(
    private val redisCacheManager: RedisCacheManager,
) : CacheManager by redisCacheManager, InitializingBean by redisCacheManager {
    @Override
    override fun getCache(name: String): Cache? {
        return redisCacheManager.getCache(name)?.let { CustomCache(it) }
    }

    class CustomCache(val cache: Cache) : Cache by cache {
        private val log = LoggerFactory.getLogger(this::class.simpleName)

        @Override
        override fun get(key: Any): Cache.ValueWrapper? {
            return try {
                cache.get(key)
            } catch (e: SerializationException) {
                log.warn("key: ${cache.name}:$key error message: ${e.message}")
                cache.evictIfPresent(key)
                null
            }
        }

        @Override
        override fun <T : Any?> get(key: Any, type: Class<T>?): T? {
            return try {
                cache.get(key, type)
            } catch (e: SerializationException) {
                log.warn("key: ${cache.name}:$key error message: ${e.message}")
                cache.evictIfPresent(key)
                null
            }
        }

        @Override
        override fun <T : Any?> get(key: Any, valueLoader: Callable<T>): T? {
            return try {
                cache.get(key, valueLoader)
            } catch (e: SerializationException) {
                log.warn("key: ${cache.name}:$key error message: ${e.message}")
                cache.evictIfPresent(key)
                null
            }
        }
    }
}

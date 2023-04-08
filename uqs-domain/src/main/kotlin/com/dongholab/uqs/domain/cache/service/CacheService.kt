package com.dongholab.uqs.domain.cache.service

import com.dongholab.uqs.domain.configuration.CustomRedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Service

@Service
class CacheService(
    private val redisTemplate: CustomRedisTemplate<String, Any>
) {

    fun evictCache(keyList: List<String>) {
        redisTemplate.delete(keyList)
    }

    fun scanCacheKeyList(pattern: String, chunkSize: Long = 10L): List<String> {
        val redisConnection = redisTemplate.connectionFactory?.connection
        val options = ScanOptions.scanOptions().match(pattern).count(chunkSize).build()
        val cursor = redisConnection?.scan(options) ?: return emptyList()

        val keyList = mutableListOf<String>()
        while (cursor.hasNext()) {
            val entry = cursor.next()
            keyList.add(String(entry, Charsets.UTF_8))
            if (keyList.size == chunkSize.toInt()) break
        }
        return keyList
    }
}

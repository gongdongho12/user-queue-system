package com.dongholab.domain.configuration

import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@EnableCaching(order = -1)
@Configuration
class CacheConfiguration(private val redisConnectionFactory: RedisConnectionFactory) {

    @Bean
    fun redisCacheManager(): CustomRedisCacheManager {
        val redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory)
            .cacheDefaults(defaultConfig())
            .withInitialCacheConfigurations(configMap())
            .build()
        return CustomRedisCacheManager(redisCacheManager)
    }

    private fun defaultConfig(): RedisCacheConfiguration {
        return RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    JdkSerializationRedisSerializer()
                )
            )
            .entryTtl(Duration.ofMinutes(30L))
    }

    private fun configMap(): Map<String, RedisCacheConfiguration> {
        val cacheConfigurations: MutableMap<String, RedisCacheConfiguration> = HashMap()

        return cacheConfigurations
    }
}

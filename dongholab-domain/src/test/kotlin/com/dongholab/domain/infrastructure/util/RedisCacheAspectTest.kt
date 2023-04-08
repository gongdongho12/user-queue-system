package com.dongholab.domain.infrastructure.util

import com.dongholab.domain.configuration.CustomRedisTemplate
import com.dongholab.domain.configuration.RedisConfiguration
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import java.util.concurrent.TimeUnit

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
@SpringBootTest(classes = [RedisCacheAspect::class, RedisConfiguration::class, CacheTest::class, AnnotationAwareAspectJAutoProxyCreator::class])
class RedisCacheAspectTest(
    private val cacheTest: CacheTest,
    private val redisTemplate: CustomRedisTemplate<String, Any>
) {

    @AfterEach
    fun deleteAllKey() {
        val keys = redisTemplate.keys("testKey*")
        redisTemplate.delete(keys)
    }

    @Test
    fun `RedisCacheable 캐시에 저장된게 없다면 저장한다`() {
        // given
        cacheTest.cacheableWithoutExpire(1, 2, 3, 4)
        // when
        val keys = redisTemplate.keys("${CacheTest.KEY}*")
        // then
        Assertions.assertThat(keys.size).isEqualTo(1)
    }

    @Test
    fun `RedisCacheable hasClassAndMethodNamePrefix = true인 경우 key생성 전략이 달라진다`() {
        // given
        cacheTest.cacheableWithPrefix(1, 2, 3, 4)
        // when
        val key = redisTemplate.keys("${CacheTest.KEY}*").toList()[0]
        // then
        Assertions.assertThat(key).contains(CacheTest::class.simpleName)
        Assertions.assertThat(key).contains("cacheableWithPrefix")
    }

    @Test
    fun `RedisCacheable 동일한 key로 호출하면 캐시를 반환한다`() {
        // given
        cacheTest.cacheableWithoutExpire(1, 2, 3, 4)
        val callCachedResult = cacheTest.cacheableWithoutExpire(1, 2, 3, 4)
        // when
        val key = redisTemplate.keys("${CacheTest.KEY}*").toList()[0]
        val cacheResult = redisTemplate.opsForValue()[key]
        // then
        Assertions.assertThat(callCachedResult).isEqualTo(cacheResult)
    }

    @Test
    fun `RedisCacheable 캐시 key가 같고 파라미터가 다르면 캐시를 새롭게 저장한다`() {
        // given
        cacheTest.cacheableWithoutExpire(1, 2, 3)
        cacheTest.cacheableWithoutExpire(1, 2, 3, 4)
        // when
        val keys = redisTemplate.keys("${CacheTest.KEY}*")
        // then
        Assertions.assertThat(keys.size).isEqualTo(2)
    }

    @Test
    fun `RedisCacheable 캐시 만료시간을 설정하지 않으면 만료되지 않는다`() {
        // given
        cacheTest.cacheableWithoutExpire(1, 2, 3, 4)
        // when
        val key = redisTemplate.keys("${CacheTest.KEY}*").toList()[0]
        // then
        Assertions.assertThat(redisTemplate.getExpire(key, TimeUnit.SECONDS)).isEqualTo(-1)
    }

    @Test
    fun `RedisCacheable expireSecond에 값을 설정하면 만료시간과 함께 저장한다`() {
        // given
        cacheTest.cacheableWithExpire(1, 2, 3, 4)
        // when
        val key = redisTemplate.keys("${CacheTest.KEY}*").toList()[0]
        // then
        Assertions.assertThat(redisTemplate.getExpire(key, TimeUnit.SECONDS)).isNotEqualTo(-1)
    }

    @Test
    fun `RedisCacheable 메서드 실행중 예외 발생가 발생하면 캐시가 저장되지 않는다`() {
        // given
        assertThrows<Exception> {
            cacheTest.cacheableWithException(1, 2, 3, 4)
        }
        // when
        val keys = redisTemplate.keys("${CacheTest.KEY}*")
        // then
        Assertions.assertThat(keys.size).isZero
    }

    @Test
    fun `RedisCachePut 캐시 만료시간을 설정하지 않으면 만료되지 않는다`() {
        // given
        cacheTest.cachePutWithoutExpire(1, 2, 3, 4)
        // when
        val key = redisTemplate.keys("${CacheTest.KEY}*").toList()[0]
        // then
        Assertions.assertThat(redisTemplate.getExpire(key, TimeUnit.SECONDS)).isEqualTo(-1)
    }

    @Test
    fun `RedisCachePut expireSecond에 값을 설정하면 만료시간과 함께 저장한다`() {
        // given
        cacheTest.cachePutWithExpire(1, 2, 3, 4)
        // when
        val key = redisTemplate.keys("${CacheTest.KEY}*").toList()[0]
        // then
        Assertions.assertThat(redisTemplate.getExpire(key, TimeUnit.SECONDS)).isNotEqualTo(-1)
    }

    @Test
    fun `RedisCachePut 메서드가 호출되면 캐시는 업데이트 된다`() {
        // given
        val result1 = cacheTest.cachePutWithoutExpire(1, 2, 3, 4)
        val result2 = cacheTest.cachePutWithReturnReverse(1, 2, 3, 4)
        // when
        val keys = redisTemplate.keys("${CacheTest.KEY}*")
        val key = keys.toList()[0]
        val cacheResult = redisTemplate.opsForValue()[key]
        // then
        println(keys)
        Assertions.assertThat(keys.size).isEqualTo(1)
        Assertions.assertThat(result1).isNotEqualTo(cacheResult)
        Assertions.assertThat(result2).isEqualTo(cacheResult)
    }

    @Test
    fun `RedisCachePut 메서드 실행중 예외 발생가 발생하면 캐시가 저장되지 않는다`() {
        // given
        assertThrows<Exception> {
            cacheTest.cachePutWithException(1, 2, 3, 4)
        }
        // when
        val keys = redisTemplate.keys("${CacheTest.KEY}*")
        // then
        Assertions.assertThat(keys.size).isZero
    }

    @Test
    fun `RedisCacheEvict 메서드가 호출되면 캐시는 제거된다`() {
        // given
        val result1 = cacheTest.cacheableWithoutExpire(1, 2, 3, 4)
        val result2 = cacheTest.cacheEvict(1, 2, 3, 4)
        // when
        val keys = redisTemplate.keys("${CacheTest.KEY}*")
        // then
        Assertions.assertThat(keys.size).isZero
    }

    @Test
    fun `RedisCacheEvict 메서드 실행중 예외 발생가 발생하면 캐시가 제거되지 않는다`() {
        // given
        cacheTest.cacheableWithoutExpire(1, 2, 3, 4)
        assertThrows<Exception> {
            cacheTest.cacheEvictWithException(1, 2, 3, 4)
        }
        // when
        val keys = redisTemplate.keys("${CacheTest.KEY}*")
        // then
        Assertions.assertThat(keys.size).isOne
    }
}

@Component
class CacheTest {
    companion object {
        const val KEY = "testKey"
        const val EXPIRE_SECOND = 100
    }

    private val logger = LoggerFactory.getLogger(this::class.simpleName)

    @RedisCacheable(key = KEY, hasClassAndMethodNamePrefix = true)
    fun cacheableWithPrefix(vararg inputs: Int): List<Int> {
        logger.info("call cacheableWithPrefix args : ${inputs.toList()}")
        return inputs.toList()
    }

    @RedisCacheable(key = KEY)
    fun cacheableWithoutExpire(vararg inputs: Int): List<Int> {
        logger.info("call cacheableWithoutExpire args : ${inputs.toList()}")
        return inputs.toList()
    }

    @RedisCacheable(key = KEY, expireSecond = 100)
    fun cacheableWithExpire(vararg inputs: Int): List<Int> {
        logger.info("call cacheableWithExpire args : ${inputs.toList()}")
        return inputs.toList()
    }

    @RedisCacheable(key = KEY)
    fun cacheableWithException(vararg inputs: Int): List<Int> {
        logger.info("call cacheableWithException args : ${inputs.toList()}")
        throw Exception()
    }

    @RedisCachePut(key = KEY)
    fun cachePutWithoutExpire(vararg inputs: Int): List<Int> {
        logger.info("call cachePutWithoutExpire args : ${inputs.toList()}")
        return inputs.toList()
    }

    @RedisCachePut(key = KEY)
    fun cachePutWithReturnReverse(vararg inputs: Int): List<Int> {
        logger.info("call cachePutWithoutExpire args : ${inputs.toList()}")
        return inputs.toList().reversed()
    }

    @RedisCachePut(key = KEY, expireSecond = 100)
    fun cachePutWithExpire(vararg inputs: Int): List<Int> {
        logger.info("call cachePutWithExpire args : ${inputs.toList()}")
        return inputs.toList()
    }

    @RedisCachePut(key = KEY)
    fun cachePutWithException(vararg inputs: Int): List<Int> {
        logger.info("call cachePutWithException args : ${inputs.toList()}")
        throw Exception()
    }

    @RedisCacheEvict(key = KEY)
    fun cacheEvict(vararg inputs: Int): List<Int> {
        logger.info("call cacheEvict args : ${inputs.toList()}")
        return inputs.toList()
    }

    @RedisCacheEvict(key = KEY)
    fun cacheEvictWithException(vararg inputs: Int): List<Int> {
        logger.info("call cacheEvictWithException args : ${inputs.toList()}")
        throw Exception()
    }
}

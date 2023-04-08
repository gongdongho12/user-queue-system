package com.dongholab.domain.infrastructure.util

/**
 * 메서드 반환 값을 캐시 저장한다.
 * 이미 저장된 캐시가 있다면 캐시 값을 리턴한다.
 * 메서드 실행중 예외 발생시 캐시가 저장되지 않는다.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RedisCacheable(
    /**
     * redis에 사용할 캐시 key
     */
    val key: String,
    /**
     * 캐시 만료 시간 (초 단위) default : 만료시간 없음
     */
    val expireSecond: Long = -1,
    /**
     *  캐시 key 생성에 class, method 이름을 사용할 것 인지 default : false
     * classAndMethodNamePrefix = true -> key::CacheService.getCache(args...)
     * classAndMethodNamePrefix = false -> key::(args...)
     */
    val hasClassAndMethodNamePrefix: Boolean = false
)

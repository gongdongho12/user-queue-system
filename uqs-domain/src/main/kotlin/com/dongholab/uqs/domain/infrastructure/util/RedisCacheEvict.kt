package com.dongholab.uqs.domain.infrastructure.util

/**
 * 메서드 실행 후 캐시를 제거한다.
 * 메서드 실행중 예외 발생시 캐시가 제거되지 않는다.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RedisCacheEvict(
    /**
     * redis에 사용할 캐시 key
     */
    val key: String,

    /**
     *  캐시 key 생성에 class, method 이름을 사용할 것 인지 default : false
     * classAndMethodNamePrefix = true -> key::CacheService.getCache(args...)
     * classAndMethodNamePrefix = false -> key::(args...)
     */
    val hasClassAndMethodNamePrefix: Boolean = false
)

package com.dongholab.domain

/** validation이나 정책인 문제로 에러가 발생할 경우 */
@Suppress("UNUSED_PARAMETER")
data class PolicyException(val errorCode: ErrorCode, val msg: String) : RuntimeException(msg)

/** 찾고자 하는 리소스가 없는 경우 */
@Suppress("UNUSED_PARAMETER")
data class DataNotFoundException(val errorCode: ErrorCode, val msg: String) : RuntimeException(msg)

/** 외부 요청(api, graphql)에 실패한 경우 */
@Suppress("UNUSED_PARAMETER")
data class CommunicationException(
    val errorCode: ErrorCode,
    val errors: List<Any>? = null,
    val extensions: Map<Any, Any>? = null,
    val msg: String? = null,
    val throwable: Throwable? = null
) : RuntimeException(errors.toString(), throwable)

/** 서버 내부에서 에러가 발생한 경우 */
@Suppress("UNUSED_PARAMETER")
data class SystemException(val errorCode: ErrorCode, val msg: String, val throwable: Throwable? = null) :
    RuntimeException(msg, throwable)

/** 이미지 업로드 과정에서 에러가 발생한 경우 */
@Suppress("UNUSED_PARAMETER")
data class ImageUploadException(val errorCode: ErrorCode, val msg: String, val throwable: Throwable? = null) :
    RuntimeException(msg, throwable)

/** 클라이언트에서 넘겨받은 파라미터가 유효하지 않은 경우 */
@Suppress("UNUSED_PARAMETER")
data class ParameterInvalidException(
    override val errorCode: ErrorCode,
    val msg: String,
    val throwable: Throwable? = null
) : CustomClientException(errorCode, msg, throwable)

/** 크롤링으로 반복해야 되는 에러가 발생한 경우 */
@Suppress("UNUSED_PARAMETER")
data class CrawlingRetryException(val errorCode: ErrorCode, val msg: String, val throwable: Throwable? = null) :
    RuntimeException(msg, throwable)

/** 4xx 클라이언트 에러 부모 class */
open class CustomClientException(open val errorCode: ErrorCode, msg: String, throwable: Throwable? = null) :
    RuntimeException(msg, throwable)

/** 일시적인 에러가 발생한 경우 (retryable) */
@Suppress("UNUSED_PARAMETER")
data class TemporaryException(val errorCode: ErrorCode, val msg: String, val throwable: Throwable? = null) :
    RuntimeException(msg, throwable)

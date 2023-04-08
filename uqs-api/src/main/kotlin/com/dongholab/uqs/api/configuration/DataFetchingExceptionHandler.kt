package com.dongholab.uqs.api.configuration

import com.dongholab.uqs.domain.CommunicationException
import com.dongholab.uqs.domain.DataNotFoundException
import com.dongholab.uqs.domain.ErrorCode
import com.dongholab.uqs.domain.ParameterInvalidException
import com.dongholab.uqs.domain.PolicyException
import com.dongholab.uqs.domain.SystemException
import com.netflix.graphql.dgs.exceptions.DgsBadRequestException
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException
import com.netflix.graphql.types.errors.TypedGraphQLError
import graphql.GraphQLError
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import org.springframework.stereotype.Component

@Component
class DataFetchingExceptionHandler : DataFetcherExceptionHandler {
    private fun createTypedGraphQLError(parameters: DataFetcherExceptionHandlerParameters): GraphQLError {
        val exception = extractException(parameters)
        notify(exception)
        return builder(exception)
            .path(parameters.path)
            .message("${exception.javaClass.simpleName}: ${exception.message}")
            .extensions(
                mapOf(
                    "code" to getErrorCode(exception),
                    "classification" to exception.javaClass.simpleName
                )
            )
            .build()
    }

    private fun notify(exception: Throwable) {
        when (exception) {
            is com.dongholab.uqs.domain.DataNotFoundException, is com.dongholab.uqs.domain.PolicyException, is com.dongholab.uqs.domain.ParameterInvalidException, is com.dongholab.uqs.domain.CommunicationException -> {}
            else -> {
            }
        }
    }

    private fun getErrorCode(exception: Throwable): com.dongholab.uqs.domain.ErrorCode {
        return when (exception) {
            is com.dongholab.uqs.domain.DataNotFoundException -> {
                exception.errorCode
            }

            is com.dongholab.uqs.domain.PolicyException -> {
                exception.errorCode
            }

            is com.dongholab.uqs.domain.ParameterInvalidException -> {
                exception.errorCode
            }

            is com.dongholab.uqs.domain.CommunicationException -> {
                exception.errorCode
            }

            is com.dongholab.uqs.domain.SystemException -> {
                exception.errorCode
            }

            else -> {
                com.dongholab.uqs.domain.ErrorCode.UNKNOWN_ERROR
            }
        }
    }

    private fun extractException(parameters: DataFetcherExceptionHandlerParameters): Throwable {
        if (parameters.exception.cause == null) {
            return parameters.exception
        }
        return parameters.exception.cause!!
    }

    private fun builder(exception: Throwable): TypedGraphQLError.Builder {
        return when (exception) {
            is DgsEntityNotFoundException -> TypedGraphQLError.newNotFoundBuilder()
            is DgsBadRequestException -> TypedGraphQLError.newBadRequestBuilder()
            else -> TypedGraphQLError.newInternalErrorBuilder()
        }
    }

    override fun onException(handlerParameters: DataFetcherExceptionHandlerParameters): DataFetcherExceptionHandlerResult {
        return DataFetcherExceptionHandlerResult.newResult()
            .error(createTypedGraphQLError(handlerParameters))
            .build()
    }
}

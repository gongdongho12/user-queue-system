package com.dongholab.api.configuration

import com.dongholab.domain.CommunicationException
import com.dongholab.domain.DataNotFoundException
import com.dongholab.domain.ErrorCode
import com.dongholab.domain.ParameterInvalidException
import com.dongholab.domain.PolicyException
import com.dongholab.domain.SystemException
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
            is DataNotFoundException, is PolicyException, is ParameterInvalidException, is CommunicationException -> {}
            else -> {
            }
        }
    }

    private fun getErrorCode(exception: Throwable): ErrorCode {
        return when (exception) {
            is DataNotFoundException -> {
                exception.errorCode
            }

            is PolicyException -> {
                exception.errorCode
            }

            is ParameterInvalidException -> {
                exception.errorCode
            }

            is CommunicationException -> {
                exception.errorCode
            }

            is SystemException -> {
                exception.errorCode
            }

            else -> {
                ErrorCode.UNKNOWN_ERROR
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

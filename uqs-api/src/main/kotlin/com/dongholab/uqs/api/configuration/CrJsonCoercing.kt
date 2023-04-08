package com.dongholab.uqs.api.configuration

import com.dongholab.uqs.domain.infrastructure.util.Strategy
import com.dongholab.uqs.domain.infrastructure.util.getObjectMapper
import com.netflix.graphql.dgs.DgsScalar
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException

@DgsScalar(name = "CrJson")
class CrJsonCoercing : Coercing<String, String> {
    override fun serialize(dataFetcherResult: Any): String {
        return dataFetcherResult.toString()
    }

    override fun parseValue(input: Any): String {
        input as String
        validate(input)
        return input
    }

    override fun parseLiteral(input: Any): String {
        val json = (input as StringValue).value
        validate(json)
        return json
    }

    private fun validate(value: String) {
        try {
            getObjectMapper(Strategy.DEFAULT).readTree(value)
        } catch (e: Exception) {
            throw CoercingParseLiteralException("Invalid Json. value = $value", e)
        }
    }
}

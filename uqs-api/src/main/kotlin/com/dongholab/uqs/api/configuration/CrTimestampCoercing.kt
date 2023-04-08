package com.dongholab.uqs.api.configuration

import com.netflix.graphql.dgs.DgsScalar
import graphql.language.IntValue
import graphql.schema.Coercing
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

@DgsScalar(name = "CrTimestamp")
class CrTimestampCoercing : Coercing<OffsetDateTime, Long> {
    override fun serialize(dataFetcherResult: Any): Long {
        dataFetcherResult as OffsetDateTime
        return dataFetcherResult.toInstant().toEpochMilli()
    }

    override fun parseValue(input: Any): OffsetDateTime {
        val timestamp = input as Long
        return millisToDateTime(timestamp)
    }

    override fun parseLiteral(input: Any): OffsetDateTime {
        return millisToDateTime((input as IntValue).value.toLong())
    }

    private fun millisToDateTime(millis: Long): OffsetDateTime {
        return Instant.ofEpochMilli(millis).atOffset(ZoneOffset.UTC)
    }
}

package com.dongholab.uqs.domain.infrastructure.util

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

fun calculateBpPercentValue(value: Long, bp: Long): Long =
    (BigDecimal(value) * (BigDecimal(bp).divide(BigDecimal(10000), MathContext.DECIMAL128)))
        .setScale(0, RoundingMode.DOWN)
        .toLong()

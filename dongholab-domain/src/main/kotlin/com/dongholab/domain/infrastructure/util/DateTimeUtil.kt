package com.dongholab.domain.infrastructure.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.Year
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun toLocalDate(yyyymmdd: String): LocalDate {
    return LocalDate.parse(yyyymmdd, DateTimeFormatter.ofPattern("yyyyMMdd"))
}

fun LocalDateTime.toDate(): java.util.Date = java.util.Date.from(this.atZone(ZoneId.systemDefault()).toInstant())

fun generateExpiration(duration: Long): java.util.Date = LocalDateTime.now().plusMinutes(duration).toDate()

fun OffsetDateTime.toCrTimestamp() = this.toInstant().toEpochMilli().toFloat()

fun now(pattern: String = "yyyy-MM-dd"): String = OffsetDateTime.now().format(DateTimeFormatter.ofPattern(pattern))

fun getKSTLocalDate(requestDate: String?): LocalDate {
    return requestDate?.let { LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
        ?: ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate()
}

fun convertKSTDateTimeToUTC(hour: Int, minute: Int, second: Int, plusDays: Long = 0L): OffsetDateTime {
    val kstNow = OffsetDateTime.now(ZoneOffset.of("+9")).plusDays(plusDays)
    return OffsetDateTime.of(
        LocalDate.of(kstNow.year, kstNow.month, kstNow.dayOfMonth),
        LocalTime.of(hour, minute, second),
        ZoneOffset.of("+9")
    ).toInstant().atOffset(ZoneOffset.UTC)
}

fun convertKSTDateTimeToUTC(localDate: LocalDate, hour: Int, minute: Int, second: Int): OffsetDateTime {
    return OffsetDateTime.of(
        localDate,
        LocalTime.of(hour, minute, second),
        ZoneOffset.of("+9")
    ).toInstant().atOffset(ZoneOffset.UTC)
}

fun firstDayOfYear(year: Int = 1900, offset: ZoneOffset = ZoneOffset.UTC): OffsetDateTime =
    OffsetDateTime.of(
        Year.of(year).atDay(1).atStartOfDay(),
        offset
    )

fun getKstDateString(plusDays: Long = 0L, pattern: String = "yyyy-MM-dd"): String {
    return OffsetDateTime.now(ZoneOffset.of("+9")).plusDays(plusDays)
        .format(DateTimeFormatter.ofPattern(pattern))
}

fun getTodayDateToUTC(hour: Int, minute: Int, second: Int): OffsetDateTime {
    val kstNow = OffsetDateTime.now(ZoneOffset.of("+9"))
    return OffsetDateTime.of(
        LocalDate.of(kstNow.year, kstNow.month, kstNow.dayOfMonth),
        LocalTime.of(hour, minute, second),
        ZoneOffset.of("+9")
    ).toInstant().atOffset(ZoneOffset.UTC)
}

fun getStartOfDayUTC(requestDate: String): OffsetDateTime {
    return LocalDate.parse(requestDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        .atTime(0, 0, 0)
        .atZone(ZoneOffset.of("+9"))
        .toOffsetDateTime().toInstant().atOffset(ZoneOffset.UTC)
}

fun getTomorrowAtStartOfDayUTC(requestDate: String): OffsetDateTime {
    return LocalDate.parse(requestDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        .atTime(0, 0, 0).plusDays(1)
        .atZone(ZoneOffset.of("+9"))
        .toOffsetDateTime().toInstant().atOffset(ZoneOffset.UTC)
}

fun getTomorrowAtEndOfDayUTC(requestDate: String): OffsetDateTime {
    return LocalDate.parse(requestDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        .atTime(23, 59, 59).plusDays(1)
        .atZone(ZoneOffset.of("+9"))
        .toOffsetDateTime().toInstant().atOffset(ZoneOffset.UTC)
}

fun getEndOfDayUTC(requestDate: String): OffsetDateTime {
    return LocalDate.parse(requestDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        .atTime(23, 59, 59)
        .atZone(ZoneOffset.of("+9"))
        .toOffsetDateTime().toInstant().atOffset(ZoneOffset.UTC)
}

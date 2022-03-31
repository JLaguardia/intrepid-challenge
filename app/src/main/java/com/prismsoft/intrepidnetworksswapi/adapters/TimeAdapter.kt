package com.prismsoft.intrepidnetworksswapi.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class TimeAdapter {
    companion object {
        private val localDateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        private val localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    @ToJson
    fun localDateTimeToString(date: LocalDateTime): String = localDateTimeFormatter.format(date)

    @FromJson
    fun toLocalDateTime(dateStr: String): LocalDateTime =
        LocalDateTime.parse(dateStr, localDateTimeFormatter)

    @ToJson
    fun localDateToString(date: LocalDate): String = localDateFormatter.format(date)

    @FromJson
    fun toLocalDate(dateStr: String): LocalDate = LocalDate.parse(dateStr, localDateFormatter)

    @ToJson
    fun instantToString(value: Instant): String = value.toLocalDateTime()

    @FromJson
    fun stringToInstant(value: String): Instant = Instant.parse(value)

    fun Instant.toLocalDateTime(pattern: String = "M/dd/yyyy h:mm a") =
        LocalDateTime.ofInstant(this, ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern(pattern))
}
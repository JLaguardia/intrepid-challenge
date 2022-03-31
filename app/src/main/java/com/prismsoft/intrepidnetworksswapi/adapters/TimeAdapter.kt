package com.prismsoft.intrepidnetworksswapi.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class TimeAdapter {
    companion object{
        private val localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        private val localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    @ToJson
    fun localDateTimeToString(date: LocalDateTime): String = localDateTimeFormatter.format(date)

    @FromJson
    fun toLocalDateTime(dateStr: String): LocalDateTime = LocalDateTime.parse(dateStr, localDateTimeFormatter)

    @ToJson
    fun localDateToString(date: LocalDate): String = localDateFormatter.format(date)

    @FromJson
    fun toLocalDate(dateStr: String): LocalDate = LocalDate.parse(dateStr, localDateFormatter)

}
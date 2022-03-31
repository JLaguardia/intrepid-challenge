package com.prismsoft.intrepidnetworksswapi.storage

import androidx.room.TypeConverter
import java.time.*

class TypeConverters {
    @TypeConverter
    fun toInstant(long: Long?) =
        if (long != null) {
            Instant.ofEpochMilli(long)
        } else {
            null
        }

    @TypeConverter
    fun fromInstant(instant: Instant?) = instant?.toEpochMilli()

    @TypeConverter
    fun toLocalDate(long: Long?) =
        if (long != null) {
            Instant.ofEpochMilli(long).atZone(ZoneId.systemDefault()).toLocalDate()
        } else {
            null
        }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?) =
        date?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()

    @TypeConverter
    fun toLocalDateTime(long: Long?) =
        if (long != null) {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(long), ZoneId.systemDefault())
        } else {
            null
        }

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?) =
        date?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
}
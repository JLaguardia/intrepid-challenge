package com.prismsoft.intrepidnetworksswapi.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@JsonClass(generateAdapter = true)
@Entity
@Parcelize
data class Episode(
    val title: String,
    @Json(name = "episode_id")
    @PrimaryKey
    val episodeNo: Int,
    @Json(name = "opening_crawl")
    val crawlText: String,
    val director: String,
    val producer: String,
    @Json(name = "release_date")
    val releaseDate: LocalDate,
    val created: LocalDateTime,
    val edited: LocalDateTime,
    val url: String
): Parcelable {
    fun formattedReleaseDate(): String =
        DateTimeFormatter.ofPattern("MMM dd, yyyy").format(releaseDate)

}
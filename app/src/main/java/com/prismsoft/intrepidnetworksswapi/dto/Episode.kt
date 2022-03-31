package com.prismsoft.intrepidnetworksswapi.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@JsonClass(generateAdapter = true)
@Entity
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
    val characters: List<String>,
    val planets: List<String>,
    val starships: List<String>,
    val vehicles: List<String>,
    val species: List<String>,
    val created: LocalDateTime,
    val edited: LocalDateTime,
    val url: String
) : Serializable {
    fun formattedReleaseDate(): String =
        DateTimeFormatter.ofPattern("MMM dd, yyyy").format(releaseDate)

}
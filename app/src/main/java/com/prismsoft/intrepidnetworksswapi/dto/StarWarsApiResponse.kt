package com.prismsoft.intrepidnetworksswapi.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StarWarsApiResponse(
    val results: List<Episode>
)
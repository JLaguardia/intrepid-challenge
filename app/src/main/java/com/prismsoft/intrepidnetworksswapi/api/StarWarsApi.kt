package com.prismsoft.intrepidnetworksswapi.api

import com.prismsoft.intrepidnetworksswapi.dto.StarWarsApiResponse
import retrofit2.http.GET

interface StarWarsApi {

    @GET("api/films/")
    suspend fun getAllEpisodes(): StarWarsApiResponse
}
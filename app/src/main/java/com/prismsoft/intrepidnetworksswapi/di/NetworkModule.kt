package com.prismsoft.intrepidnetworksswapi.di

import com.prismsoft.intrepidnetworksswapi.BuildConfig
import com.prismsoft.intrepidnetworksswapi.api.StarWarsApi
import com.prismsoft.intrepidnetworksswapi.adapters.TimeAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = Kodein.Module("networkModule") {
    bind<Moshi>() with singleton {
        Moshi.Builder()
            .add(TimeAdapter())
            .build()
    }

    bind<OkHttpClient>() with singleton {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    bind<Retrofit>() with singleton {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SW_URL)
            .client(instance())
            .addConverterFactory(MoshiConverterFactory.create(instance()))
            .build()
    }

    bind<StarWarsApi>() with singleton {
        val retro: Retrofit = instance()
        retro.create(StarWarsApi::class.java)
    }
}
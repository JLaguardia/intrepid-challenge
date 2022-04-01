package com.prismsoft.intrepidnetworksswapi.di

import com.prismsoft.intrepidnetworksswapi.BuildConfig
import com.prismsoft.intrepidnetworksswapi.api.StarWarsApi
import com.prismsoft.intrepidnetworksswapi.adapters.TimeAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = DI.Module("networkModule") {
    bind<Moshi>() with singleton {
        Moshi.Builder()
            .add(TimeAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    bind<OkHttpClient>() with singleton {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
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
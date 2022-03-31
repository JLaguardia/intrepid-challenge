package com.prismsoft.intrepidnetworksswapi.dto

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.parcelize.Parcelize

abstract class NavigationArg<T: Parcelable>(protected val moshi: Moshi): NavType<T>(false){
    protected abstract val adapter: JsonAdapter<T>

    override fun parseValue(value: String): T = adapter.fromJson(value)!!

    override fun put(bundle: Bundle, key: String, value: T) = bundle.putParcelable(key, value)
}

class EpisodeListNavArg(moshi: Moshi): NavigationArg<ListWrapper>(moshi) {
    override val adapter: JsonAdapter<ListWrapper> = moshi.adapter(ListWrapper::class.java)
    override fun get(bundle: Bundle, key: String): ListWrapper? = bundle.getParcelable(key)
}

class EpisodeNavArg(moshi: Moshi): NavigationArg<Episode>(moshi) {
    override val adapter: JsonAdapter<Episode> = moshi.adapter(Episode::class.java)
    override fun get(bundle: Bundle, key: String): Episode? = bundle.getParcelable(key)
}

@Parcelize
data class ListWrapper(val eps: List<Episode>): Parcelable
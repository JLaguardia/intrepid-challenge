package com.prismsoft.intrepidnetworksswapi.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.prismsoft.intrepidnetworksswapi.dto.Episode

interface EpisodeDatabase {
    fun episodeDao(): EpisodeDao
}

@Database(
    entities = [
        Episode::class
    ],
    version = 1
)
@TypeConverters(com.prismsoft.intrepidnetworksswapi.storage.TypeConverters::class)
abstract class AppDatabase : RoomDatabase(), EpisodeDatabase {
    companion object {
        fun initialize(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "epDb").build()
        }
    }

    abstract override fun episodeDao(): EpisodeDao
}
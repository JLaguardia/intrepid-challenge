package com.prismsoft.intrepidnetworksswapi.storage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prismsoft.intrepidnetworksswapi.dto.Episode
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EpisodeDao {

    @Query("select * from Episode")
    abstract fun get(): LiveData<List<Episode>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(episodes: List<Episode>)

    @Query("select * from Episode order by title desc")
    abstract fun getSortByNameDesc(): LiveData<List<Episode>>

    @Query("select * from Episode order by title")
    abstract fun getSortByName(): LiveData<List<Episode>>

    @Query("select * from Episode order by releaseDate")
    abstract fun getSortByRelease(): LiveData<List<Episode>>

    @Query("select * from Episode where episodeNo = :id")
    abstract fun getById(id: Int): Flow<Episode?>
}
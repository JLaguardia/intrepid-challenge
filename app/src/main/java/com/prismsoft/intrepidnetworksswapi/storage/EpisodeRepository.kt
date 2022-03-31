package com.prismsoft.intrepidnetworksswapi.storage

import android.util.Log
import androidx.lifecycle.LiveData
import com.prismsoft.intrepidnetworksswapi.SortEnum
import com.prismsoft.intrepidnetworksswapi.api.StarWarsApi
import com.prismsoft.intrepidnetworksswapi.dto.Episode
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {
    fun getAll(): LiveData<List<Episode>>
    fun getAllSorted(type: SortEnum): LiveData<List<Episode>>
    suspend fun fetch(): Boolean
    fun getById(epId: Int): Flow<Episode?>
}

class EpisodeRepositoryImpl(
    private val api: StarWarsApi,
    private val dao: EpisodeDao
) : EpisodeRepository {
    override fun getAll(): LiveData<List<Episode>> = dao.get()

    override fun getAllSorted(type: SortEnum): LiveData<List<Episode>> =
        when (type) {
            SortEnum.TITLE_ASC -> dao.getSortByName()
            SortEnum.TITLE_DESC -> dao.getSortByNameDesc()
            SortEnum.EPISODE_NUM -> dao.get()
            else -> dao.getSortByRelease()
        }


    override suspend fun fetch(): Boolean = try {
        val response = api.getAllEpisodes()
        dao.insertAll(response.results)
        true
    } catch (e: Exception) {
        Log.e("EpisodeRepository", "There was a problem: ${e.localizedMessage}")
        false
    }

    override fun getById(epId: Int) = dao.getById(epId)

}
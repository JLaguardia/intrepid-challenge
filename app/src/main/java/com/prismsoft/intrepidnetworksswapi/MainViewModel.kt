package com.prismsoft.intrepidnetworksswapi

import androidx.lifecycle.*
import androidx.navigation.NavController
import com.prismsoft.intrepidnetworksswapi.dto.Episode
import com.prismsoft.intrepidnetworksswapi.storage.EpisodeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

class MainViewModel(private val repository: EpisodeRepository) : BaseViewModel() {

    /**
     * Used for updating the list when mutated
     */
    private val _sort: MutableStateFlow<SortEnum> = MutableStateFlow(SortEnum.RELEASE_DATE)
    fun setSortType(type: SortEnum) {
        _sort.value = type
    }

    /**
     * Fetches the episodes from the api and populates the db.
     * State is set according to the result in order to start
     * observing.
     */
    fun getEpisodes() {
        viewModelScope.launch(Dispatchers.IO) { repository.fetch() }
    }

    /**
     * Get all episodes from repository - using switch map for dynamic sorting
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllEpisodes() = _sort.flatMapLatest { repository.getAllSorted(it) }

    fun getEpisodeById(epId: Int): Flow<Episode?> = repository.getById(epId)
}

enum class SortEnum {
    RELEASE_DATE,
    EPISODE_NUM,
    TITLE_ASC,
    TITLE_DESC
}
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
     * State to observe for data changes.
     * I have found that this is not necessary for Compose
     */
    sealed class MainState : State {
        data class DataFetched(val navController: NavController) : MainState()
        data class Error(val navController: NavController) : MainState()
    }

    /**
     * Fetches the episodes from the api and populates the db.
     * State is set according to the result in order to start
     * observing.
     */
    fun getEpisodes(navController: NavController) {
        viewModelScope.launch(Dispatchers.IO) {
            val fetchSuccessful = repository.fetch()
            setState(
                if (fetchSuccessful) {
                    MainState.DataFetched(navController)
                } else {
                    MainState.Error(navController)
                }
            )
        }
    }

    /**
     * Get all episodes from repository - using switch map for dynamic sorting
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllEpisodes() = _sort.flatMapLatest { repository.getAllSorted(it) }

    fun getEpisodeByIdFlow(epId: Int): Flow<Episode?> = repository.getById(epId)
}

enum class SortEnum {
    RELEASE_DATE,
    EPISODE_NUM,
    TITLE_ASC,
    TITLE_DESC
}

//no longer necessary, but keeping it in just in case
fun <T, K, R> LiveData<T>.combineWith(
    secondData: LiveData<K>,
    block: (T?, K?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block.invoke(this.value, secondData.value)
    }
    result.addSource(secondData) {
        result.value = block.invoke(this.value, secondData.value)
    }
    return result
}
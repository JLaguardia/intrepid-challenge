package com.prismsoft.intrepidnetworksswapi

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.prismsoft.intrepidnetworksswapi.adapters.TimeAdapter
import com.prismsoft.intrepidnetworksswapi.api.StarWarsApi
import com.prismsoft.intrepidnetworksswapi.dto.Episode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MainViewModel(
//    private val api: StarWarsApi //DI wasnt working, not enough time to figure out why
) : BaseViewModel() {

    var api: StarWarsApi? = null
    private val _sort: MutableLiveData<SortEnum> = MutableLiveData(SortEnum.RELEASE_DATE)
    var sortType = SortEnum.RELEASE_DATE
    set(value) {
        field = value
        _sort.postValue(sortType)
        //todo finish sorting Transformations. switchmap stuff
    }

    sealed class MainState : State() {
        data class DataLoaded(val episodes: List<Episode>, val navController: NavController) :
            MainState()

        data class Error(val navController: NavController) : MainState()
    }

    fun getEpisodes(navController: NavController) {
        Log.w("MainViewModel","getEpisodes called")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                api?.getAllEpisodes()?.let { response ->
//                    MediatorLiveData<Pair<SortEnum, List<Episode>>>
                    //insert into db
                    Log.w("MainViewModel","got episodes count: ${response.results.size}")
                    setState(MainState.DataLoaded(response.results.sortedBy { it.episodeNo }, navController))
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "There was an exception: ${e.localizedMessage}")
                setState(MainState.Error(navController))
            }
        }
    }


}

enum class SortEnum{
    RELEASE_DATE,
    EPISODE_NUM,
    TITLE_ASC,
    TITLE_DESC
}
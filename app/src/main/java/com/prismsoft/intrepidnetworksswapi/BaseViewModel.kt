package com.prismsoft.intrepidnetworksswapi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged

abstract class BaseViewModel: ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData(State.Reset)
    val state: LiveData<State> = _state.distinctUntilChanged()

    sealed class State{
        object Reset: State()
    }

    fun setState(newState: State){
        _state.postValue(newState)
    }

    fun resetState(){ _state.value = State.Reset }

    override fun onCleared() {
        resetState()
        super.onCleared()
    }
}
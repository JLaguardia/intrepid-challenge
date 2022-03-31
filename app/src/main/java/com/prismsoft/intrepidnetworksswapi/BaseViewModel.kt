package com.prismsoft.intrepidnetworksswapi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged

interface State
object Reset: State

abstract class BaseViewModel: ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData(Reset)
    val state: LiveData<State> = _state.distinctUntilChanged()

    fun setState(newState: State){
        _state.postValue(newState)
    }

    fun resetState(){ _state.value = Reset }

    override fun onCleared() {
        resetState()
        super.onCleared()
    }
}
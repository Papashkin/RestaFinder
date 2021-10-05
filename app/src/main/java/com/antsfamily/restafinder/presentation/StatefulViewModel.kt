package com.antsfamily.restafinder.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class StatefulViewModel<State>(private val initialState: State) : ViewModel() {

    private val _state = MutableLiveData(initialState)
    val state: LiveData<State>
        get() = _state

    protected fun changeState(changeState: (currentState: State) -> State) {
        _state.value = changeState(_state.value ?: initialState)
    }
}

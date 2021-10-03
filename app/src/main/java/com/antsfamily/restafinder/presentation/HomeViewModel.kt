package com.antsfamily.restafinder.presentation

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : StatefulViewModel<HomeViewModel.State>(State()) {

    data class State(
        val dateTime: String? = null,
        val isProgramsLoading: Boolean = true,
        val isProgramsVisible: Boolean = false,
    )

    fun onResume() {
        //TODO
    }
}

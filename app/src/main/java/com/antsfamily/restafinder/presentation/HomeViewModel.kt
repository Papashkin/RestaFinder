package com.antsfamily.restafinder.presentation

import javax.inject.Inject

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

package com.antsfamily.restafinder.presentation

import androidx.lifecycle.viewModelScope
import com.antsfamily.restafinder.data.local.model.Coordinates
import com.antsfamily.restafinder.data.remote.model.Restaurant
import com.antsfamily.restafinder.data.remote.model.RestaurantsList
import com.antsfamily.restafinder.domain.usecase.DataFetchingTimerFlow
import com.antsfamily.restafinder.domain.usecase.GetCoordinatesUseCase
import com.antsfamily.restafinder.domain.usecase.GetRestaurantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCoordinatesUseCase: GetCoordinatesUseCase,
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val timerFlow: DataFetchingTimerFlow,
) : StatefulViewModel<HomeViewModel.State>(State()) {

    data class State(
        val isStartLoadingVisible: Boolean = true,
        val isErrorVisible: Boolean = false,
        val restaurants: List<Restaurant> = emptyList(),
        val isRestaurantsVisible: Boolean = false,
        val isFetchLoadingVisible: Boolean = false,
    )

    private var coordinates: Coordinates? = null
    private var isTimerStarted: Boolean = false

    private val isDataAlreadyReceived: Boolean
        get() = state.value?.restaurants?.isEmpty() == false

    fun onResume() {
        getCoordinates()
    }

    fun onRetryButtonClick() {
        coordinates?.let {
            showStartLoading()
            getRestaurants(it)
        }
    }

    private fun getCoordinates() {
        getCoordinatesUseCase(
            params = Unit,
            onResult = {
                coordinates = it
                getRestaurants(it)
            },
            onError = ::handleErrorResult
        )
    }

    private fun getRestaurants(coordinates: Coordinates) {
        getRestaurantsUseCase(
            params = coordinates,
            onResult = ::handleGetRestaurantsSuccessResult,
            onError = ::handleErrorResult
        )
    }

    private fun handleGetRestaurantsSuccessResult(result: RestaurantsList) {
        changeState {
            it.copy(
                isStartLoadingVisible = false,
                isFetchLoadingVisible = false,
                isRestaurantsVisible = true,
                restaurants = result.results.take(RESTAURANTS_LIST_SIZE),
                isErrorVisible = false
            )
        }
        if (!isTimerStarted) {
            launchFetchingTimer()
            isTimerStarted = true
        }
    }

    private fun handleErrorResult(exception: Exception) {
        changeState {
            it.copy(
                isErrorVisible = !isDataAlreadyReceived,
                isStartLoadingVisible = false,
                isFetchLoadingVisible = false,
                isRestaurantsVisible = isDataAlreadyReceived,
            )
        }
    }

    private fun launchFetchingTimer() = viewModelScope.launch {
        timerFlow(DataFetchingTimerFlow.Params(DELAY_SECONDS, INIT_DELAY_SECONDS))
            .collect {
                showFetchLoading()
                getCoordinates()
            }
    }

    private fun showStartLoading() {
        changeState {
            it.copy(
                isStartLoadingVisible = true,
                isFetchLoadingVisible = false,
                isRestaurantsVisible = false,
                isErrorVisible = false
            )
        }
    }

    private fun showFetchLoading() {
        changeState { it.copy(isFetchLoadingVisible = true) }
    }

    companion object {
        private const val RESTAURANTS_LIST_SIZE = 15
        private const val DELAY_SECONDS = 10000L
        private const val INIT_DELAY_SECONDS = 10000L
    }
}

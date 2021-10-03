package com.antsfamily.restafinder.presentation

import com.antsfamily.restafinder.data.local.model.Coordinates
import com.antsfamily.restafinder.data.remote.model.Restaurant
import com.antsfamily.restafinder.data.remote.model.RestaurantsList
import com.antsfamily.restafinder.domain.usecase.GetCoordinatesUseCase
import com.antsfamily.restafinder.domain.usecase.GetRestaurantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCoordinatesUseCase: GetCoordinatesUseCase,
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
) : StatefulViewModel<HomeViewModel.State>(State()) {

    data class State(
        val isStartLoadingVisible: Boolean = true,
        val isErrorVisible: Boolean = false,
        val isNetworkErrorVisible: Boolean = false,
        val restaurants: List<Restaurant> = emptyList(),
        val isRestaurantsVisible: Boolean = false,
        val isFetchLoadingVisible: Boolean = false,
    )

    init {
        getCoordinates()
    }

    fun onResume() {
        //TODO
    }

    private fun getCoordinates() {
        getCoordinatesUseCase(
            params = Unit,
            onResult = ::getRestaurants,
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
    }

    private fun handleErrorResult(exception: Exception) {
        if (!isDataAlreadyReceived()) {
            changeState {
                it.copy(
                    isErrorVisible = true,
                    isStartLoadingVisible = false,
                    isFetchLoadingVisible = false,
                    isRestaurantsVisible = false,
                )
            }
        }
    }

    private fun isDataAlreadyReceived(): Boolean =
        state.value?.restaurants?.isEmpty() == false

    companion object {
        private const val RESTAURANTS_LIST_SIZE = 15
    }
}

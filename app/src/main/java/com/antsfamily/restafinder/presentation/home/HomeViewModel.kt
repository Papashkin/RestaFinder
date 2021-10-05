package com.antsfamily.restafinder.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.antsfamily.restafinder.data.DataRepository
import com.antsfamily.restafinder.data.local.CoordinatesProvider
import com.antsfamily.restafinder.data.local.DataFetchingTimer
import com.antsfamily.restafinder.data.local.model.Coordinates
import com.antsfamily.restafinder.domain.entity.Restaurants
import com.antsfamily.restafinder.presentation.Event
import com.antsfamily.restafinder.presentation.StatefulViewModel
import com.antsfamily.restafinder.presentation.TextResource
import com.antsfamily.restafinder.presentation.home.model.RestaurantItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val coordinatesProvider: CoordinatesProvider,
    private val repository: DataRepository,
    private val timer: DataFetchingTimer
) : StatefulViewModel<HomeViewModel.State>(State()) {

    data class State(
        val isStartLoadingVisible: Boolean = true,
        val isErrorVisible: Boolean = false,
        val restaurants: List<RestaurantItem> = emptyList(),
        val isRestaurantsVisible: Boolean = false,
        val isFetchLoadingVisible: Boolean = false,
    )

    private val _showSnackBar = MutableLiveData<Event<TextResource>>()
    val showSnackBar: LiveData<Event<TextResource>>
        get() = _showSnackBar

    private var coordinates: Coordinates? = null
    private var timerJob: Job? = null
    private var isTimerStarted: Boolean = false
    private var favouriteRestaurantsIds: List<String> = emptyList()

    private val isDataAlreadyReceived: Boolean
        get() = state.value?.restaurants?.isEmpty() == false

    init {
        getFavouriteRestaurantsIds()
    }

    fun onResume() {
        getCoordinates()
    }

    fun onPause() {
        timerJob?.cancel()
        timerJob = null
        isTimerStarted = false
        setFavouriteRestaurantsIds()
    }

    fun onRetryButtonClick() {
        coordinates?.let {
            showStartLoading()
            getRestaurants(it)
        }
    }

    fun onFavouriteIconClick(item: RestaurantItem) {
        if (favouriteRestaurantsIds.contains(item.id)) {
            removeFromFavourites(item.id)
        } else {
            addToFavourites(item.id)
        }
    }

    private fun addToFavourites(id: String) {
        favouriteRestaurantsIds = favouriteRestaurantsIds.plus(id)
        changeState {
            it.copy(
                restaurants = it.restaurants.map { item ->
                    item.copy(isFavourite = if (item.id == id) true else item.isFavourite)
                }
            )
        }
        _showSnackBar.postValue(Event(TextResource.RESTAURANT_ADD_TO_FAVOURITES))
    }

    private fun removeFromFavourites(id: String) {
        favouriteRestaurantsIds = favouriteRestaurantsIds.minus(id)
        changeState {
            it.copy(
                restaurants = it.restaurants.map { item ->
                    item.copy(isFavourite = if (item.id == id) false else item.isFavourite)
                }
            )
        }
        _showSnackBar.postValue(Event(TextResource.RESTAURANT_REMOVE_FROM_FAVOURITES))
    }

    private fun getFavouriteRestaurantsIds() = viewModelScope.launch {
        try {
            favouriteRestaurantsIds = repository.getFavouriteRestaurantIds()
        } catch (e: java.lang.Exception) {
            // no-op
        }
    }

    private fun setFavouriteRestaurantsIds() = viewModelScope.launch {
        try {
            repository.setFavouriteRestaurantIds(favouriteRestaurantsIds)
        } catch (e: java.lang.Exception) {
            // no-op
        }
    }

    private fun getCoordinates() {
        coordinatesProvider.getCoordinates().let {
            coordinates = it
            getRestaurants(it)
        }
    }

    private fun getRestaurants(coordinates: Coordinates) = viewModelScope.launch {
        try {
            val result = repository.getRestaurants(coordinates)
            handleGetRestaurantsSuccessResult(result)
        } catch (e: Exception) {
            handleErrorResult(e)
        }
    }

    private fun handleGetRestaurantsSuccessResult(result: Restaurants) {
        changeState {
            it.copy(
                isStartLoadingVisible = false,
                isFetchLoadingVisible = false,
                isRestaurantsVisible = true,
                restaurants = getRestaurantItems(result),
                isErrorVisible = false
            )
        }
        if (!isTimerStarted) {
            launchFetchingTimer()
            isTimerStarted = true
        }
    }

    private fun getRestaurantItems(data: Restaurants): List<RestaurantItem> =
        data.results.take(RESTAURANTS_LIST_SIZE).map {
            RestaurantItem(
                id = it.id.id,
                address = it.address,
                name = it.name.firstOrNull()?.value.orEmpty(),
                description = it.description.firstOrNull()?.value.orEmpty(),
                image = it.image,
                isFavourite = favouriteRestaurantsIds.contains(it.id.id)
            )
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

    private fun launchFetchingTimer() {
        timerJob = viewModelScope.launch {
            timer.run(DataFetchingTimer.Params(DELAY_SECONDS, INIT_DELAY_SECONDS))
                .cancellable()
                .collect {
                    viewModelScope.ensureActive()
                    showFetchLoading()
                    getCoordinates()
                }
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

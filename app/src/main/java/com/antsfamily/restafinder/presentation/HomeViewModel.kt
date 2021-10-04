package com.antsfamily.restafinder.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.antsfamily.restafinder.data.local.model.Coordinates
import com.antsfamily.restafinder.data.local.model.RestaurantItem
import com.antsfamily.restafinder.data.remote.model.Restaurant
import com.antsfamily.restafinder.data.remote.model.RestaurantsList
import com.antsfamily.restafinder.domain.usecase.*
import com.antsfamily.restafinder.utils.noop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCoordinatesUseCase: GetCoordinatesUseCase,
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val getFavouriteRestaurantsIdsUseCase: GetFavouriteRestaurantsIdsUseCase,
    private val setFavouriteRestaurantsIdsUseCase: SetFavouriteRestaurantsIdsUseCase,
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

    private fun getFavouriteRestaurantsIds() {
        getFavouriteRestaurantsIdsUseCase(Unit, {
            favouriteRestaurantsIds = it
        }, {
            noop()
        })
    }

    private fun setFavouriteRestaurantsIds() {
        setFavouriteRestaurantsIdsUseCase(favouriteRestaurantsIds, ::noop, ::noop)
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
                restaurants = getRestaurantItems(result.results),
                isErrorVisible = false
            )
        }
        if (!isTimerStarted) {
            launchFetchingTimer()
            isTimerStarted = true
        }
    }

    private fun getRestaurantItems(data: List<Restaurant>): List<RestaurantItem> =
        data.take(RESTAURANTS_LIST_SIZE).map {
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

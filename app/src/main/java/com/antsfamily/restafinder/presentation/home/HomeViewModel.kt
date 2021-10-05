package com.antsfamily.restafinder.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.antsfamily.restafinder.data.DataRepository
import com.antsfamily.restafinder.data.local.CoordinatesProvider
import com.antsfamily.restafinder.data.local.model.Coordinates
import com.antsfamily.restafinder.domain.entity.Restaurants
import com.antsfamily.restafinder.presentation.Event
import com.antsfamily.restafinder.presentation.StatefulViewModel
import com.antsfamily.restafinder.presentation.TextResource
import com.antsfamily.restafinder.presentation.home.model.RestaurantItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val coordinatesProvider: CoordinatesProvider,
    private val repository: DataRepository
) : StatefulViewModel<HomeViewModel.State>(State()) {

    data class State(
        val isStartLoadingVisible: Boolean = true,
        val isErrorVisible: Boolean = false,
        val restaurants: List<RestaurantItem> = emptyList(),
        val isRestaurantsVisible: Boolean = false,
        val isFetchLoadingVisible: Boolean = false,
        val isNetworkConnectionBannerVisible: Boolean = false,
    )

    private val _showSnackBar = MutableLiveData<Event<TextResource>>()
    val showSnackBar: LiveData<Event<TextResource>>
        get() = _showSnackBar

    private var coordinates: Coordinates? = null
    private var favouriteRestaurantsIds: List<String> = emptyList()
    private var isCoordinatesProviderActive: Boolean = false

    private val isDataAlreadyReceived: Boolean
        get() = state.value?.restaurants?.isEmpty() == false

    init {
        getFavouriteRestaurantsIds()
    }

    fun onResume() {
        if (!isCoordinatesProviderActive) {
            isCoordinatesProviderActive = true
            getCoordinates()
        }
    }

    fun onPause() {
        coordinatesProvider.cancel()
        isCoordinatesProviderActive = false
        setFavouriteRestaurantsIds()
    }

    fun onNetworkAvailable() {
        hideNetworkConnectionBanner()
        if (!isCoordinatesProviderActive) {
            getCoordinates()
            isCoordinatesProviderActive = true
        }
    }

    fun onNetworkLost() {
        coordinatesProvider.cancel()
        isCoordinatesProviderActive = false
        if (isDataAlreadyReceived) {
            showNetworkConnectionBanner()
        }
    }

    fun onRetryButtonClick() {
        coordinates?.let {
            showStartLoading()
            getRestaurants(it)
        }
    }

    fun onErrorBannerCloseButtonClick() {
        hideNetworkConnectionBanner()
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
        } catch (e: Exception) {
            // no-op
        }
    }

    private fun setFavouriteRestaurantsIds() = viewModelScope.launch {
        try {
            repository.setFavouriteRestaurantIds(favouriteRestaurantsIds)
        } catch (e: Exception) {
            // no-op
        }
    }

    private fun getCoordinates() = viewModelScope.launch {
        coordinatesProvider.run(CoordinatesProvider.Params(DELAY_SECONDS)) {
            coordinates = it
            getRestaurants(it)
        }
    }

    private fun getRestaurants(coordinates: Coordinates) = viewModelScope.launch {
        try {
            if (isDataAlreadyReceived) showFetchLoading()
            val result = repository.getRestaurants(coordinates)
            handleGetRestaurantsSuccessResult(result)
        } catch (e: Exception) {
            handleErrorResult()
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

    private fun handleErrorResult() {
        changeState {
            it.copy(
                isErrorVisible = !isDataAlreadyReceived,
                isStartLoadingVisible = false,
                isFetchLoadingVisible = false,
                isRestaurantsVisible = isDataAlreadyReceived,
            )
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

    private fun showNetworkConnectionBanner() {
        postChangeState { it.copy(isNetworkConnectionBannerVisible = true) }
    }

    private fun hideNetworkConnectionBanner() {
        postChangeState { it.copy(isNetworkConnectionBannerVisible = false) }
    }

    companion object {
        private const val RESTAURANTS_LIST_SIZE = 15
        private const val DELAY_SECONDS = 10000L
    }
}

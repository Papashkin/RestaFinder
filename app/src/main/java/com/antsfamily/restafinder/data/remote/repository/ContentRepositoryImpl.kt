package com.antsfamily.restafinder.data.remote.repository

import com.antsfamily.restafinder.data.local.SharedPrefs
import com.antsfamily.restafinder.data.remote.model.RestaurantsList
import com.antsfamily.restafinder.domain.repository.ContentRepository
import com.antsfamily.restafinder.remote.RestaurantService
import javax.inject.Inject

class ContentRepositoryImpl @Inject constructor(
    private val service: RestaurantService,
    private val sharedPrefs: SharedPrefs
) : ContentRepository {

    override suspend fun getRestaurants(latitude: Double, longitude: Double): RestaurantsList =
        service.getRestaurants(latitude, longitude)

    override suspend fun getFavouriteRestaurantIds(): List<String> =
        sharedPrefs.favouriteRestaurantsIds.toList()

    override suspend fun setFavouriteRestaurantIds(ids: List<String>) {
        sharedPrefs.favouriteRestaurantsIds = ids.toSet()
    }
}

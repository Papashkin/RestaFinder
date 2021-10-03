package com.antsfamily.restafinder.data.remote.repository

import com.antsfamily.restafinder.data.remote.model.RestaurantsList
import com.antsfamily.restafinder.domain.repository.ContentRepository
import com.antsfamily.restafinder.remote.RestaurantService
import javax.inject.Inject

class ContentRepositoryImpl @Inject constructor(
    private val service: RestaurantService
) : ContentRepository {

    override suspend fun getRestaurants(latitude: Double, longitude: Double): RestaurantsList =
        service.getRestaurants(latitude, longitude)
}

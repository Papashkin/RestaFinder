package com.antsfamily.restafinder.data.remote

import com.antsfamily.restafinder.domain.entity.Restaurants
import javax.inject.Inject

class RemoteSourceImpl @Inject constructor(
    private val service: RestaurantService,
) : RemoteSource {

    override suspend fun getRestaurants(latitude: Double, longitude: Double): Restaurants =
        service.getRestaurants(latitude, longitude)
}

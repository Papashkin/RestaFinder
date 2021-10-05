package com.antsfamily.restafinder.data

import com.antsfamily.restafinder.data.local.model.Coordinates
import com.antsfamily.restafinder.domain.entity.Restaurants

interface DataRepository {
    suspend fun getRestaurants(coordinates: Coordinates): Restaurants
    suspend fun getFavouriteRestaurantIds(): List<String>
    suspend fun setFavouriteRestaurantIds(ids: List<String>)
}

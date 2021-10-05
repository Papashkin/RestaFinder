package com.antsfamily.restafinder.data

import com.antsfamily.restafinder.domain.entity.Restaurants

interface DataRepository {
    suspend fun getRestaurants(latitude: Double, longitude: Double): Restaurants
    suspend fun getFavouriteRestaurantIds(): List<String>
    suspend fun setFavouriteRestaurantIds(ids: List<String>)
}

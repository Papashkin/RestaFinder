package com.antsfamily.restafinder.domain.repository

import com.antsfamily.restafinder.data.remote.RestaurantsList

interface ContentRepository {
    suspend fun getRestaurants(latitude: Double, longitude: Double): RestaurantsList
}
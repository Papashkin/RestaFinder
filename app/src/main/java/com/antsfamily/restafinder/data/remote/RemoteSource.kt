package com.antsfamily.restafinder.data.remote

import com.antsfamily.restafinder.domain.entity.Restaurants

interface RemoteSource {
    suspend fun getRestaurants(latitude: Double, longitude: Double): Restaurants
}

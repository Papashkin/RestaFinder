package com.antsfamily.restafinder.data.remote

import com.antsfamily.restafinder.domain.entity.Restaurants
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantService {

    @GET("venues")
    suspend fun getRestaurants(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Restaurants
}

package com.antsfamily.restafinder.remote

import com.antsfamily.restafinder.data.remote.RestaurantsList
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantService {

    @GET("venues")
    suspend fun getRestaurants(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): RestaurantsList
}

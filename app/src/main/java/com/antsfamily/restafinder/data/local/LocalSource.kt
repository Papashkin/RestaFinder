package com.antsfamily.restafinder.data.local

interface LocalSource {
    suspend fun getFavouriteRestaurantIds(): List<String>
    suspend fun setFavouriteRestaurantIds(ids: List<String>)
}

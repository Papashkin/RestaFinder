package com.antsfamily.restafinder.data.local

import javax.inject.Inject

class LocalSourceImpl @Inject constructor(
    private val sharedPrefs: SharedPrefs
) : LocalSource {

    override suspend fun getFavouriteRestaurantIds(): List<String> =
        sharedPrefs.favouriteRestaurantsIds.toList()

    override suspend fun setFavouriteRestaurantIds(ids: List<String>) {
        sharedPrefs.favouriteRestaurantsIds = ids.toSet()
    }
}

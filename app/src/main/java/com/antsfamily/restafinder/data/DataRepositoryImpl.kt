package com.antsfamily.restafinder.data

import com.antsfamily.restafinder.data.local.LocalSource
import com.antsfamily.restafinder.data.local.model.Coordinates
import com.antsfamily.restafinder.data.remote.RemoteSource
import com.antsfamily.restafinder.domain.entity.Restaurants
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val localSource: LocalSource,
    private val remoteSource: RemoteSource
) : DataRepository {
    override suspend fun getRestaurants(coordinates: Coordinates): Restaurants = with(coordinates) {
        remoteSource.getRestaurants(latitude, longitude)
    }

    override suspend fun getFavouriteRestaurantIds(): List<String> =
        localSource.getFavouriteRestaurantIds()

    override suspend fun setFavouriteRestaurantIds(ids: List<String>) =
        localSource.setFavouriteRestaurantIds(ids)
}

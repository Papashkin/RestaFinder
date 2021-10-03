package com.antsfamily.restafinder.domain.usecase

import com.antsfamily.restafinder.data.local.model.Coordinates
import com.antsfamily.restafinder.data.remote.model.RestaurantsList
import com.antsfamily.restafinder.domain.base.BaseUseCase
import com.antsfamily.restafinder.domain.repository.ContentRepository
import javax.inject.Inject

class GetRestaurantsUseCase @Inject constructor(
    private val repository: ContentRepository
): BaseUseCase<Coordinates, RestaurantsList>() {

    override suspend fun run(params: Coordinates): RestaurantsList {
        return repository.getRestaurants(
            latitude = params.latitude,
            longitude = params.longitude
        )
    }
}

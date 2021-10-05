package com.antsfamily.restafinder.domain.usecase

import com.antsfamily.restafinder.data.DataRepository
import com.antsfamily.restafinder.data.local.model.Coordinates
import com.antsfamily.restafinder.domain.entity.Restaurants
import com.antsfamily.restafinder.domain.base.BaseUseCase
import javax.inject.Inject

class GetRestaurantsUseCase @Inject constructor(
    private val repository: DataRepository
): BaseUseCase<Coordinates, Restaurants>() {

    override suspend fun run(params: Coordinates): Restaurants {
        return repository.getRestaurants(
            latitude = params.latitude,
            longitude = params.longitude
        )
    }
}

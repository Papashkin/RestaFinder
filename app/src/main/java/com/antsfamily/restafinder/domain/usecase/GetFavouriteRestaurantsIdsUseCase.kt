package com.antsfamily.restafinder.domain.usecase

import com.antsfamily.restafinder.data.DataRepository
import com.antsfamily.restafinder.domain.base.BaseUseCase
import javax.inject.Inject

class GetFavouriteRestaurantsIdsUseCase @Inject constructor(
    private val repository: DataRepository
) : BaseUseCase<Unit, List<String>>() {

    override suspend fun run(params: Unit): List<String> = repository.getFavouriteRestaurantIds()
}

package com.antsfamily.restafinder.domain.usecase

import com.antsfamily.restafinder.data.DataRepository
import com.antsfamily.restafinder.domain.base.BaseUseCase
import javax.inject.Inject

class SetFavouriteRestaurantsIdsUseCase @Inject constructor(
    private val repository: DataRepository
) : BaseUseCase<List<String>, Unit>() {

    override suspend fun run(params: List<String>) = repository.setFavouriteRestaurantIds(params)
}

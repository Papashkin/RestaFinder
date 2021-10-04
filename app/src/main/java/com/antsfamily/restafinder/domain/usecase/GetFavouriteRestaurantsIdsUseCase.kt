package com.antsfamily.restafinder.domain.usecase

import com.antsfamily.restafinder.domain.base.BaseUseCase
import com.antsfamily.restafinder.domain.repository.ContentRepository
import javax.inject.Inject

class GetFavouriteRestaurantsIdsUseCase @Inject constructor(
    private val repository: ContentRepository
) : BaseUseCase<Unit, List<String>>() {

    override suspend fun run(params: Unit): List<String> = repository.getFavouriteRestaurantIds()
}

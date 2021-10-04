package com.antsfamily.restafinder.domain.usecase

import com.antsfamily.restafinder.domain.base.BaseUseCase
import com.antsfamily.restafinder.domain.repository.ContentRepository
import javax.inject.Inject

class SetFavouriteRestaurantsIdsUseCase @Inject constructor(
    private val repository: ContentRepository
) : BaseUseCase<List<String>, Unit>() {

    override suspend fun run(params: List<String>) = repository.setFavouriteRestaurantIds(params)
}

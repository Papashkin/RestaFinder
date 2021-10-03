package com.antsfamily.restafinder.domain.usecase

import com.antsfamily.restafinder.data.local.StaticFields
import com.antsfamily.restafinder.data.local.model.Coordinates
import com.antsfamily.restafinder.domain.base.BaseUseCase
import java.util.*
import javax.inject.Inject

class GetCoordinatesUseCase @Inject constructor(): BaseUseCase<Unit, Coordinates>() {

    override suspend fun run(params: Unit): Coordinates {
        val coordinates = StaticFields.COORDINATES.first()
        Collections.rotate(StaticFields.COORDINATES, -1)
        return coordinates
    }
}

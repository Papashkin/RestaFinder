package com.antsfamily.restafinder.data.local

import com.antsfamily.restafinder.data.local.model.Coordinates
import java.util.*

class CoordinatesProvider {

    fun getCoordinates(): Coordinates {
        val coordinates = COORDINATES.first()
        Collections.rotate(COORDINATES, OFFSET)
        return coordinates
    }

    companion object {
        private const val OFFSET = -1
        private val COORDINATES = mutableListOf(
            Coordinates(60.170187, 24.930599),
            Coordinates(60.169418, 24.931618),
            Coordinates(60.169818, 24.932906),
            Coordinates(60.170005, 24.935105),
            Coordinates(60.169108, 24.93621),
            Coordinates(60.168355, 24.934869),
            Coordinates(60.167560, 24.932562),
            Coordinates(60.168254, 24.931532),
            Coordinates(60.169012, 24.930341),
            Coordinates(60.170085, 24.929569),
        )
    }
}

package com.antsfamily.restafinder.data.local

import com.antsfamily.restafinder.data.local.model.Coordinates
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.util.*

class CoordinatesProvider {

    private var job: Job? = null

    suspend fun run(
        params: Params,
        onCoordinatesReceived: (coordinates: Coordinates) -> Unit = {}
    ) {
        job = GlobalScope.launch(Dispatchers.Default) {
            run(params.delay).collect {
                onCoordinatesReceived(it)
            }
        }
    }

    fun cancel() {
        job?.cancel()
        job = null
    }

    private fun run(delay: Long) = flow {
        while (true) {
            emit(getCoordinates())
            delay(delay)
        }
    }

    private fun getCoordinates(): Coordinates {
        val coordinates = COORDINATES.first()
        Collections.rotate(COORDINATES, OFFSET)
        return coordinates
    }

    data class Params(
        val delay: Long,
        val initialDelay: Long = 0,
    )

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

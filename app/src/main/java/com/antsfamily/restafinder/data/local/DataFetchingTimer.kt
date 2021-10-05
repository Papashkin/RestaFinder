package com.antsfamily.restafinder.data.local

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DataFetchingTimer {

    fun run(params: Params): Flow<Unit> = flow {
        delay(params.initialDelay)
        while (true) {
            emit(Unit)
            delay(params.delay)
        }
    }

    data class Params(
        val delay: Long,
        val initialDelay: Long = 0,
    )
}

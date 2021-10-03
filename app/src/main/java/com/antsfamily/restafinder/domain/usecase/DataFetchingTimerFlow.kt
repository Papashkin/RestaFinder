package com.antsfamily.restafinder.domain.usecase

import com.antsfamily.restafinder.domain.base.FlowUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * param [Long] here is a value for delay.
 * In our data fetching cases it should be 10 seconds.
 */
class DataFetchingTimerFlow @Inject constructor() :
    FlowUseCase<DataFetchingTimerFlow.Params, Unit>() {

    override fun run(params: Params): Flow<Unit> = flow {
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

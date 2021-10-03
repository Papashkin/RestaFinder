package com.antsfamily.restafinder.domain.base

import kotlinx.coroutines.flow.Flow

abstract class FlowUseCase<in Params, out Type> {

    abstract fun run(params: Params): Flow<Type>

    operator fun invoke(params: Params): Flow<Type> = run(params)
}

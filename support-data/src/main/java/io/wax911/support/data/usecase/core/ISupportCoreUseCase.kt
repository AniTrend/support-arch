package io.wax911.support.data.usecase.core

import io.wax911.support.data.usecase.contract.IUseCase

/**
 * Use case representative for non coroutine related context
 */
interface ISupportCoreUseCase<P, R> : IUseCase {

    /**
     * Solves a given use case in the implementation target
     *
     * @param param input for solving a given use case
     */
    operator fun invoke(param: P): R
}
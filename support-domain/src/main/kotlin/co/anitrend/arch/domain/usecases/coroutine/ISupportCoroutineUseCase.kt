package co.anitrend.arch.domain.usecases.coroutine

import co.anitrend.arch.domain.common.IUseCase

/**
 * Use case representative for a coroutine related context
 */
interface ISupportCoroutineUseCase<P, R> : IUseCase {

    /**
     * Solves a given use case in the implementation target
     *
     * @param param input for solving a given use case
     */
    suspend operator fun invoke(param: P): R
}
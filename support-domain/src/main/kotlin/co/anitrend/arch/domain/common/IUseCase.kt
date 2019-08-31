package co.anitrend.arch.domain.common

/**
 * Common use case type
 */
interface IUseCase {

    /**
     * Informs underlying repositories or related components running background operations to stop
     */
    fun onCleared()
}
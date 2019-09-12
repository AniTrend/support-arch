package co.anitrend.arch.data.repository.contract

/**
 * Repository contract with support for canceling coroutines [onCleared]
 *
 * @since v1.1.0
 */
interface ISupportRepository {

    /**
     * Deals with cancellation of any pending or on going operations that the repository
     * might be working on
     */
    fun onCleared()
}
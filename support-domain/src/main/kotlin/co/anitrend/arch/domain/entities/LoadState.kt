package co.anitrend.arch.domain.entities

/**
 * State representing ongoing, completed or failed operations
 */
sealed class LoadState {

    /**
     * Load state is idle
     */
    object Idle : LoadState()

    /**
     * Load state is successful
     */
    object Success : LoadState()

    /**
     * Load state is loading
     *
     * @param position Where the loader should be placed
     */
    data class Loading(
        val position: Position = Position.TOP
    ) : LoadState() {
        enum class Position { TOP, BOTTOM }
    }

    /**
     * Load state for failed loading
     *
     * @param details General [Throwable] type with details regarding the error
     */
    data class Error(
        val details: Throwable
    ) : LoadState()
}
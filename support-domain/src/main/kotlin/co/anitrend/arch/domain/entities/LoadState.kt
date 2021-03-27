package co.anitrend.arch.domain.entities

/**
 * State representing ongoing, completed or failed operations
 *
 * @property position Where the loader should be placed
 */
sealed class LoadState {

    abstract val position: Position

    /**
     * State position identifier
     */
    enum class Position { TOP, BOTTOM, UNDEFINED }

    /**
     * Load state is idle
     */
    data class Idle(
        override val position: Position = Position.UNDEFINED
    ) : LoadState()

    /**
     * Load state is successful
     */
    data class Success(
        override val position: Position = Position.UNDEFINED
    ) : LoadState()

    /**
     * Load state is loading
     */
    data class Loading(
        override val position: Position = Position.UNDEFINED
    ) : LoadState()

    /**
     * Load state for failed loading
     *
     * @param details General [Throwable] type with details regarding the error
     */
    data class Error(
        val details: Throwable,
        override val position: Position = Position.UNDEFINED
    ) : LoadState()
}
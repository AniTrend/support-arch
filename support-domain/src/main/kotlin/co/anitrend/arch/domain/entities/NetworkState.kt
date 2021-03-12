package co.anitrend.arch.domain.entities

@Deprecated(
    "Replace with LoadState",
    level = DeprecationLevel.WARNING,
    replaceWith = ReplaceWith(
        "LoadState",
        "co.anitrend.arch.domain.entities.LoadState"
    )
)
/**
 * State representing ongoing, completed or failed requests
 */
sealed class NetworkState {

    /**
     * Represents a state of idle
     */
    @Suppress("DEPRECATION")
    object Idle : NetworkState()

    /**
     * Represents a network state of loading
     */
    @Suppress("DEPRECATION")
    object Loading : NetworkState()

    /**
     * Represents a network state that has succeeded
     */
    @Suppress("DEPRECATION")
    object Success : NetworkState()

    /**
     * Network state for failed requests with an optional message or code
     *
     * @param code The response code from the last request
     * @param heading Heading related to the error message
     * @param message Message to display if any are available
     */
    @Suppress("DEPRECATION")
    data class Error(
        val code: Int? = null,
        val heading: String? = null,
        val message: String? = null
    ) : NetworkState()
}
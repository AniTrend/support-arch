package co.anitrend.arch.domain.entities

/**
 * State representing ongoing, completed or failed requests
 */
sealed class NetworkState {

    /**
     * Represents a state of idle
     */
    object Idle : NetworkState()

    /**
     * Represents a network state of loading
     */
    object Loading : NetworkState()

    /**
     * Represents a network state that has succeeded
     */
    object Success : NetworkState()

    /**
     * Network state for failed requests with an optional message or code
     *
     * @param code The response code from the last request
     * @param heading Heading related to the error message
     * @param message Message to display if any are available
     */
    data class Error(
        val code: Int? = null,
        val heading: String? = null,
        val message: String? = null
    ) : NetworkState()
}
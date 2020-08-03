package co.anitrend.arch.extension.network

/**
 * Connection state representative
 *
 * @since v1.3.0
 */
sealed class ConnectivityState {
    /** Unknown connectivity state */
    object Unknown : ConnectivityState()
    /** Connected to one or more interfaces with internet capabilities */
    object Connected : ConnectivityState()
    /** Disconnected, no interfaces with internet capability */
    object Disconnected : ConnectivityState()
}
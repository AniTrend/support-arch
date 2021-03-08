package co.anitrend.arch.domain.state

/**
 * Contract for user interface state which UI components can use to interact with state
 *
 * @property networkState Network request status to show to the user
 * @property refreshState Refresh status to show to the user. Separate from [networkState],
 * this value is importantly only when refresh is requested
 * @property refresh Refreshes & invalidates underlying data source fetches it from scratch.
 * @property retry Retries any failed requests.
 */
abstract class UiState<out T> {
    abstract val networkState: T
    abstract val refreshState: T
    abstract val refresh: suspend () -> Unit
    abstract val retry: suspend () -> Unit
}
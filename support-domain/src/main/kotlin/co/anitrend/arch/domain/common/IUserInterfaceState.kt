package co.anitrend.arch.domain.common

/**
 * Contract for user interface state which UI components can use to interact with state
 *
 * @property networkState Network request status to show to the user
 * @property refreshState Refresh status to show to the user. Separate from [networkState],
 * this value is importantly only when refresh is requested
 * @property refresh Refreshes & invalidates underlying data source fetches it from scratch.
 * @property retry Retries any failed requests.
 */
interface IUserInterfaceState<out T> {
    val networkState: T
    val refreshState: T
    val refresh: () -> Unit
    val retry: () -> Unit
}
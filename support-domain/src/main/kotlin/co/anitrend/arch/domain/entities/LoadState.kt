package co.anitrend.arch.domain.entities

/**
 * State representing ongoing, completed or failed operations
 */
sealed class LoadState {

    object Loading : LoadState()
    object Success : LoadState()
    data class Error(
        val exception: Throwable
    ) : LoadState()
}
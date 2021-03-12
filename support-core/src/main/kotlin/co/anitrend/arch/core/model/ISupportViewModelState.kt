package co.anitrend.arch.core.model

import androidx.lifecycle.LiveData
import co.anitrend.arch.domain.entities.LoadState

/**
 * A helper contract for view model items
 *
 * @property model observable model from a use case output
 * @property networkState observable network state from underlying sources
 * @property refreshState observable network refresh state from underlying sources
 *
 * @param R type of your [model]
 *
 * @since v1.3.0
 */
interface ISupportViewModelState<R> {

    val model: LiveData<R?>
    val networkState: LiveData<LoadState>?
    val refreshState: LiveData<LoadState>?

    /**
     * Triggers use case to perform a retry operation
     */
    suspend fun retry()

    /**
     * Triggers use case to perform refresh operation
     */
    suspend fun refresh()

    /**
     * Called upon [androidx.lifecycle.ViewModel.onCleared] and should optionally
     * call cancellation of any ongoing jobs.
     *
     * If your use case source is of type [co.anitrend.arch.domain.common.IUseCase]
     * then you could optionally call [co.anitrend.arch.domain.common.IUseCase.onCleared] here
     */
    fun onCleared()
}
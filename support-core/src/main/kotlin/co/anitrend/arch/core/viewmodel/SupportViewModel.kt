package co.anitrend.arch.core.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.Transformations.map
import co.anitrend.arch.core.viewmodel.contract.ISupportViewModel
import co.anitrend.arch.data.model.UserInterfaceState
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.domain.usecases.core.ISupportCoreUseCase

/**
 * A helper view model class that provides models, network states to the calling view
 *
 * @param state optional initial state to use
 * @since v0.9.X
 */
abstract class SupportViewModel<P, R>(
    private val state: SavedStateHandle? = null
) : ViewModel(), ISupportViewModel<P, R> {

    protected abstract val useCase: ISupportCoreUseCase<P, UserInterfaceState<R>>

    private val payload = MutableLiveData<P>()

    private val useCaseResult: LiveData<UserInterfaceState<R>> =
        map(payload) { useCase(it) }

    override val model: LiveData<R?> =
        Transformations.switchMap(useCaseResult) { it.model }

    override val networkState: LiveData<NetworkState>? =
        Transformations.switchMap(useCaseResult) { it.networkState }


    override val refreshState: LiveData<NetworkState>? =
        Transformations.switchMap(useCaseResult) { it.refreshState }

    /**
     * Forwards queries for the repository to handle
     *
     * @param payload request data to be used by the repository
     */
    override fun invoke(payload: P) {
        this.payload.value = payload
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    override fun onCleared() {
        useCase.onCleared()
        super.onCleared()
    }

    /**
     * Requests the repository to perform a retry operation
     */
    override fun retry() {
        val uiModel = useCaseResult.value
        uiModel?.retry?.invoke()
    }

    /**
     * Requests the repository to perform a refreshAndInvalidate operation on the underlying database
     */
    override fun refresh() {
        val uiModel = useCaseResult.value
        uiModel?.refresh?.invoke()
    }
}

/*
 *    Copyright 2019 AniTrend
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package co.anitrend.arch.core.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.Transformations.map
import co.anitrend.arch.core.viewmodel.contract.ISupportViewModel
import co.anitrend.arch.data.model.UserInterfaceState
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.domain.usecases.ISupportUseCase

/**
 * A helper view model class that provides models, network states to the calling view
 *
 * @since v0.9.X
 */
abstract class SupportPagingViewModel<P, R> : ViewModel(), ISupportViewModel<P, R> {

    protected abstract val useCase: ISupportUseCase<P, UserInterfaceState<R>>

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
     * Starts view model operations
     *
     * @param parameter request payload
     */
    override fun invoke(parameter: P) {
        this.payload.value = parameter
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

package io.wax911.sample.data.usecase.show

import androidx.paging.PagedList
import co.anitrend.arch.data.model.UserInterfaceState
import io.wax911.sample.data.entitiy.show.ShowEntity
import io.wax911.sample.data.repository.show.ShowPagedRepository
import io.wax911.sample.domain.usecases.show.TraktShowUseCase

class ShowPagedListUseCase(
    private val showPagedRepository: ShowPagedRepository
) : TraktShowUseCase<UserInterfaceState<PagedList<ShowEntity>>>(showPagedRepository) {

    /**
     * Informs underlying repositories or related components running background operations to stop
     */
    override fun onCleared() {
        showPagedRepository.onCleared()
    }
}
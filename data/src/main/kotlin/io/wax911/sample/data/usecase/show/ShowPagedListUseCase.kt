package io.wax911.sample.data.usecase.show

import androidx.paging.PagedList
import io.wax911.sample.data.entitiy.show.ShowEntity
import io.wax911.sample.data.repository.show.ShowPagedRepository
import io.wax911.sample.domain.repositories.show.ITraktShowRepository
import io.wax911.sample.domain.usecases.show.TraktShowUseCase
import co.anitrend.arch.data.model.UserInterfaceState

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
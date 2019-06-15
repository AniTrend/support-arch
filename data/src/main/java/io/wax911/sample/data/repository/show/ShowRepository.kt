package io.wax911.sample.data.repository.show

import androidx.paging.PagedList
import io.wax911.sample.data.model.show.Show
import io.wax911.sample.data.usecase.media.contract.IPagedMediaUseCase
import io.wax911.sample.data.usecase.media.show.ShowPagedListUseCase
import io.wax911.support.data.repository.SupportRepository

class ShowRepository(
    private val showPagedListUseCase: IPagedMediaUseCase<Show>
) : SupportRepository<PagedList<Show>, IPagedMediaUseCase.Payload>() {

    /**
     * Handles dispatching of network requests to a background thread
     *
     * @param subject subject to apply business rules
     */
    override fun invoke(subject: IPagedMediaUseCase.Payload) =
        showPagedListUseCase(subject)
}
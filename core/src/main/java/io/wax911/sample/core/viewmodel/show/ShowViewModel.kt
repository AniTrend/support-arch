package io.wax911.sample.core.viewmodel.show

import androidx.paging.PagedList
import io.wax911.sample.data.model.show.Show
import io.wax911.sample.data.usecase.media.contract.IPagedMediaUseCase
import io.wax911.support.core.viewmodel.SupportViewModel
import io.wax911.support.data.repository.contract.ISupportRepository

class ShowViewModel(
    showRepository: ISupportRepository<PagedList<Show>, IPagedMediaUseCase.Payload>
) : SupportViewModel<PagedList<Show>, IPagedMediaUseCase.Payload>(showRepository)
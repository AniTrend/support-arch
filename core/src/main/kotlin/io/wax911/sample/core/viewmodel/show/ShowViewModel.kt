package io.wax911.sample.core.viewmodel.show

import androidx.paging.PagedList
import co.anitrend.arch.core.viewmodel.SupportPagingViewModel
import co.anitrend.arch.core.viewmodel.SupportViewModel
import io.wax911.sample.data.entitiy.show.ShowEntity
import io.wax911.sample.data.usecase.show.ShowPagedListUseCase
import io.wax911.sample.domain.usecases.show.TraktShowUseCase

class ShowViewModel(
    override val useCase: ShowPagedListUseCase
) : SupportPagingViewModel<TraktShowUseCase.Payload, PagedList<ShowEntity>>()
package io.wax911.sample.core.viewmodel.show

import androidx.paging.PagedList
import io.wax911.sample.data.entitiy.show.ShowEntity
import io.wax911.sample.data.usecase.show.ShowPagedListUseCase
import io.wax911.sample.domain.usecases.show.TraktShowUseCase
import co.anitrend.arch.core.viewmodel.SupportViewModel

class ShowViewModel(
    override val useCase: ShowPagedListUseCase
) : SupportViewModel<TraktShowUseCase.Payload, PagedList<ShowEntity>>()
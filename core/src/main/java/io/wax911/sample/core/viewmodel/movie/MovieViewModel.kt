package io.wax911.sample.core.viewmodel.movie

import androidx.paging.PagedList
import io.wax911.sample.data.model.movie.Movie
import io.wax911.sample.data.usecase.media.contract.IPagedMediaUseCase
import io.wax911.support.core.viewmodel.SupportViewModel
import io.wax911.support.data.repository.contract.ISupportRepository

class MovieViewModel(
    movieRepository: ISupportRepository<PagedList<Movie>, IPagedMediaUseCase.Payload>
) : SupportViewModel<PagedList<Movie>, IPagedMediaUseCase.Payload>(movieRepository)
package io.wax911.sample.movie.viewmodel

import androidx.paging.PagedList
import co.anitrend.arch.core.viewmodel.SupportPagingViewModel
import co.anitrend.arch.core.viewmodel.SupportViewModel
import io.wax911.sample.data.entitiy.movie.MovieEntity
import io.wax911.sample.data.usecase.movie.MoviePagedListUseCase
import io.wax911.sample.domain.usecases.movie.TraktMovieUseCase

class MovieViewModel(
    override val useCase: MoviePagedListUseCase
) : SupportPagingViewModel<TraktMovieUseCase.Payload, PagedList<MovieEntity>>()
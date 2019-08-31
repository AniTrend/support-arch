package io.wax911.sample.core.viewmodel.movie

import androidx.paging.PagedList
import io.wax911.sample.data.entitiy.movie.MovieEntity
import io.wax911.sample.data.usecase.movie.MoviePagedListUseCase
import io.wax911.sample.domain.usecases.movie.TraktMovieUseCase
import co.anitrend.arch.core.viewmodel.SupportViewModel

class MovieViewModel(
    override val useCase: MoviePagedListUseCase
) : SupportViewModel<TraktMovieUseCase.Payload, PagedList<MovieEntity>>()
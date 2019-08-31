package io.wax911.sample.data.usecase.movie

import androidx.paging.PagedList
import co.anitrend.arch.data.model.UserInterfaceState
import io.wax911.sample.data.entitiy.movie.MovieEntity
import io.wax911.sample.data.repository.movie.MoviePagedRepository
import io.wax911.sample.domain.usecases.movie.TraktMovieUseCase

class MoviePagedListUseCase(
    private val moviePagedRepository: MoviePagedRepository
) : TraktMovieUseCase<UserInterfaceState<PagedList<MovieEntity>>>(moviePagedRepository) {

    /**
     * Informs underlying repositories or related components running background operations to stop
     */
    override fun onCleared() {
        moviePagedRepository.onCleared()
    }
}
package io.wax911.sample.data.repository.movie

import androidx.paging.PagedList
import co.anitrend.arch.data.model.UserInterfaceState
import co.anitrend.arch.data.repository.SupportRepository
import io.wax911.sample.data.datasource.remote.movie.contract.TraktMoviePagedSource
import io.wax911.sample.data.entitiy.movie.MovieEntity
import io.wax911.sample.domain.repositories.movie.ITraktMovieRepository

class MoviePagedRepository(
    private val source: TraktMoviePagedSource
) : SupportRepository(source), ITraktMovieRepository<UserInterfaceState<PagedList<MovieEntity>>> {

    /**
     * @return popular movies
     */
    override fun getPopularMovies(): UserInterfaceState<PagedList<MovieEntity>> {
        return UserInterfaceState.create(
            model = source.popularMovieObservable(null),
            source = source
        )
    }

    /**
     * @return trending movies
     */
    override fun getTrendingMovies(): UserInterfaceState<PagedList<MovieEntity>> {
        return UserInterfaceState.create(
            model = source.trendingMovieObservable(null),
            source = source
        )
    }
}
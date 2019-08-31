package io.wax911.sample.domain.repositories.movie

import co.anitrend.arch.domain.common.IUserInterfaceState

interface ITraktMovieRepository<R: IUserInterfaceState<*>> {

    /**
     * @return popular movies
     */
    fun getPopularMovies(): R

    /**
     * @return trending movies
     */
    fun getTrendingMovies(): R
}
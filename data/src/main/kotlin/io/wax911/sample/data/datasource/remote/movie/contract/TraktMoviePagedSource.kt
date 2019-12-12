package io.wax911.sample.data.datasource.remote.movie.contract

import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.data.source.paging.SupportPagingDataSource
import co.anitrend.arch.extension.SupportDispatchers
import io.wax911.sample.data.datasource.local.query.MovieDao
import io.wax911.sample.data.datasource.remote.common.MediaPagedSource
import io.wax911.sample.data.entitiy.movie.MovieEntity

abstract class TraktMoviePagedSource(
    dispatchers: SupportDispatchers
) : MediaPagedSource<MovieEntity>(dispatchers) {

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see MovieDao.getPopularItems
     */
    abstract val popularMovieObservable: ISourceObservable<Nothing?, PagedList<MovieEntity>>

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see MovieDao.getPopularItems
     */
    abstract val trendingMovieObservable: ISourceObservable<Nothing?, PagedList<MovieEntity>>
}
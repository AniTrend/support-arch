package io.wax911.sample.data.datasource.remote.movie.contract

import androidx.paging.PagedList
import io.wax911.sample.data.datasource.local.query.MovieDao
import io.wax911.sample.data.entitiy.movie.MovieEntity
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.data.source.paging.SupportPagingDataSource

abstract class TraktMoviePagedSource : SupportPagingDataSource<MovieEntity>() {

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see MovieDao.getPopularItems
     */
    abstract val popularMovieObservable: ISourceObservable<PagedList<MovieEntity>, Nothing?>

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see MovieDao.getPopularItems
     */
    abstract val trendingMovieObservable: ISourceObservable<PagedList<MovieEntity>, Nothing?>
}
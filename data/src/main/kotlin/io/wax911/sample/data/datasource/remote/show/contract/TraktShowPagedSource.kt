package io.wax911.sample.data.datasource.remote.show.contract

import androidx.paging.PagedList
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.data.source.paging.SupportPagingDataSource
import io.wax911.sample.data.datasource.local.query.ShowDao
import io.wax911.sample.data.entitiy.show.ShowEntity

abstract class TraktShowPagedSource : SupportPagingDataSource<ShowEntity>() {

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see ShowDao.getPopularItems
     */
    abstract val popularShowObservable: ISourceObservable<PagedList<ShowEntity>, Nothing?>

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see ShowDao.getPopularItems
     */
    abstract val trendingShowObservable: ISourceObservable<PagedList<ShowEntity>, Nothing?>
}
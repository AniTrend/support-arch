package io.wax911.sample.data.datasource.remote.show.contract

import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.data.source.paging.SupportPagingDataSource
import io.wax911.sample.data.datasource.local.query.ShowDao
import io.wax911.sample.data.datasource.remote.common.MediaPagedSource
import io.wax911.sample.data.entitiy.show.ShowEntity

abstract class TraktShowPagedSource : MediaPagedSource<ShowEntity>() {

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see ShowDao.getPopularItems
     */
    abstract val popularShowObservable: ISourceObservable<Nothing?, PagedList<ShowEntity>>

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     *
     * @see ShowDao.getPopularItems
     */
    abstract val trendingShowObservable: ISourceObservable<Nothing?, PagedList<ShowEntity>>
}
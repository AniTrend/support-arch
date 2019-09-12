package io.wax911.sample.data.repository.show

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import co.anitrend.arch.data.model.UserInterfaceState
import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.domain.entities.NetworkState
import io.wax911.sample.data.datasource.remote.show.contract.TraktShowPagedSource
import io.wax911.sample.data.entitiy.show.ShowEntity
import io.wax911.sample.domain.repositories.show.ITraktShowRepository

class ShowPagedRepository(
    private val showPagedDataSource: TraktShowPagedSource
) : SupportRepository(showPagedDataSource), ITraktShowRepository<UserInterfaceState<PagedList<ShowEntity>>> {

    override fun getPopularShows(): UserInterfaceState<PagedList<ShowEntity>> {
        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            MutableLiveData<NetworkState>()
        }

        return UserInterfaceState(
            model = showPagedDataSource.popularShowObservable(null),
            networkState = showPagedDataSource.networkState,
            refresh = {
                showPagedDataSource.invalidateAndRefresh()
                refreshTrigger.value = null
            },
            refreshState = refreshState,
            retry = {
                showPagedDataSource.retryRequest()
            }
        )
    }

    override fun getTrendingShows(): UserInterfaceState<PagedList<ShowEntity>> {
        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            MutableLiveData<NetworkState>()
        }

        return UserInterfaceState(
            model = showPagedDataSource.trendingShowObservable(null),
            networkState = showPagedDataSource.networkState,
            refresh = {
                showPagedDataSource.invalidateAndRefresh()
                refreshTrigger.value = null
            },
            refreshState = refreshState,
            retry = {
                showPagedDataSource.retryRequest()
            }
        )
    }
}
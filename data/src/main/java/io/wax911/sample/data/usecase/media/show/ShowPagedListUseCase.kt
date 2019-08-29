package io.wax911.sample.data.usecase.media.show

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import io.wax911.sample.data.api.endpoint.ShowEndpoint
import io.wax911.sample.data.dao.query.ShowDao
import io.wax911.sample.data.model.show.Show
import io.wax911.sample.data.source.show.ShowPagingDataSource
import io.wax911.sample.data.usecase.media.contract.IPagedMediaUseCase
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.model.UiModel

class ShowPagedListUseCase(
    private val showEndpoint: ShowEndpoint,
    private val showDao: ShowDao
) : IPagedMediaUseCase<Show> {

    /**
     * Solves a given use case in the implementation target
     *
     * @param param input for solving a given use case
     */
    override fun invoke(param: IPagedMediaUseCase.Payload): UiModel<PagedList<Show>> {
        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data.
        val dataSource = ShowPagingDataSource(
            showEndpoint = showEndpoint,
            showDao = showDao,
            payload = param
        )

        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            MutableLiveData<NetworkState>()
        }

        return UiModel(
            model = dataSource.shows.invoke(param),
            networkState = dataSource.networkState,
            refresh = {
                dataSource.invalidateAndRefresh()
                refreshTrigger.value = null
            },
            refreshState = refreshState,
            retry = {
                dataSource.retryRequest()
            }
        )
    }
}
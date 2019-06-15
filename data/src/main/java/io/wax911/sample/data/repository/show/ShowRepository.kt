package io.wax911.sample.data.repository.show

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import io.wax911.sample.data.api.endpoint.ShowEndpoint
import io.wax911.sample.data.dao.query.ShowDao
import io.wax911.sample.data.model.show.Show
import io.wax911.sample.data.source.ShowPagingDataSource
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.model.UiModel
import io.wax911.support.data.repository.SupportRepository

class ShowRepository(
    private val showEndpoint: ShowEndpoint,
    private val showDao: ShowDao
) : SupportRepository<PagedList<Show>, Bundle>() {

    /**
     * Handles dispatching of network requests to a background thread
     *
     * @param parameter parameter/s for the request
     */
    override fun invoke(parameter: Bundle): UiModel<PagedList<Show>> {

        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data.
        val dataSource = ShowPagingDataSource(
            bundle = parameter,
            showEndpoint = showEndpoint,
            showDao = showDao
        )

        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            MutableLiveData<NetworkState>()
        }

        return UiModel(
            model = dataSource.shows.invoke(parameter),
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
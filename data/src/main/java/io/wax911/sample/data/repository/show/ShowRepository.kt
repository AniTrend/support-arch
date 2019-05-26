package io.wax911.sample.data.repository.show

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import io.wax911.sample.data.api.endpoint.ShowEndpoint
import io.wax911.sample.data.model.show.Show
import io.wax911.sample.data.source.ShowDataSource
import io.wax911.support.data.factory.contract.IRetrofitFactory
import io.wax911.support.data.repository.SupportRepository
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.model.UiModel
import org.koin.core.inject

class ShowRepository : SupportRepository<PagedList<Show>>() {

    override val retroFactory by inject<IRetrofitFactory>()

    /**
     * Handles dispatching of network requests to a background thread
     *
     * @param bundle bundle of parameters for the request
     */
    override fun invokeRequest(bundle: Bundle): UiModel<PagedList<Show>> {
        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data.
        val boundaryCallback = ShowDataSource(
            bundle = bundle,
            showEndpoint = retroFactory.createService(
                ShowEndpoint::class.java
            )
        )
        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            MutableLiveData<NetworkState>()
        }

        return UiModel(
            model = boundaryCallback.observerOnLiveDataWith(bundle),
            networkState = boundaryCallback.networkState,
            refresh = {
                boundaryCallback.refreshOrInvalidate()
                refreshTrigger.value = null
            },
            refreshState = refreshState,
            retry = {
                boundaryCallback.pagingRequestHelper.retryAllFailed()
            }
        )
    }
}
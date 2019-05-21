package io.wax911.sample.core.repository.show

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.paging.toLiveData
import io.wax911.sample.core.dao.DatabaseHelper
import io.wax911.sample.core.data.source.PopularShowDataSource
import io.wax911.sample.core.model.show.Show
import io.wax911.sample.core.util.StateUtil.PAGING_CONFIGURATION
import io.wax911.support.core.controller.contract.ISupportRequestClient
import io.wax911.support.core.repository.SupportBoundaryCallback.Companion.IO_EXECUTOR
import io.wax911.support.core.repository.SupportRepository
import io.wax911.support.core.view.model.NetworkState
import io.wax911.support.core.view.model.UiModel
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Response

class PopularShowRepository(
    context: Context
) : SupportRepository<PagedList<Show>, List<Show>>(context), KoinComponent {

    override val networkClient: ISupportRequestClient by inject()
    private val databaseHelper: DatabaseHelper by inject()

    /**
     * When the application is connected to the internet this method is called to resolve the
     * kind of content that needs to be fetched from the network using the given parameters
     *
     * @param bundle bundle of parameters for the request
     */
    override fun requestFromNetwork(bundle: Bundle): UiModel<PagedList<Show>> {
        val sourceFactory = PopularShowDataSource.Factory(bundle)
        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val livePagedList = sourceFactory.toLiveData(
            config = PAGING_CONFIGURATION,
            // provide custom executor for network requests, otherwise it will default to
            // Arch Components' IO pool which is also used for disk access
            fetchExecutor = IO_EXECUTOR
        )

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }

        return UiModel(
            model = livePagedList,
            networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.networkState
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.refreshOrInvalidate()
            },
            refreshState = refreshState,
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailedRequests()
            }
        )
    }

    /**
     * When the application is not connected to the internet this method is called to resolve the
     * kind of content that needs to be fetched from the database using the given parameters
     *
     * @param bundle bundle of parameters for the request
     */
    override fun requestFromCache(bundle: Bundle): UiModel<PagedList<Show>> {
        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data.
        val boundaryCallback = PopularShowDataSource.BoundaryCallback(
                bundle = bundle,
                responseHandler = this::insertResultIntoDb
        )
        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh(bundle)
        }

        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val livePagedList = databaseHelper.showDao().getPopularItems().toLiveData(
            config = PAGING_CONFIGURATION,
            boundaryCallback = boundaryCallback
        )

        return UiModel(
            model = livePagedList,
            networkState = boundaryCallback.networkState,
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState,
            retry = {
                boundaryCallback.pagingRequestHelper.retryAllFailed()
            }
        )
    }

    /**
     * When refresh is called, we simply run a fresh network request and when it arrives, clear
     * the database table and insert all new items in a transaction.
     *
     * Since the PagedList already uses a database bound data source, it will automatically be
     * updated after the database transaction is finished.
     */
    override fun refresh(bundle: Bundle): LiveData<NetworkState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Inserts the response into the database while also assigning position indices to items.
     */
    override fun insertResultIntoDb(bundle: Bundle, value: Response<List<Show>?>) {
        launch {
            value.body()?.apply {
                databaseHelper.showDao().insert(*toTypedArray())
            }
        }
    }
}
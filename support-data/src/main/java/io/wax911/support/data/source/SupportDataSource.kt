package io.wax911.support.data.source

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.room.RoomDatabase
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.source.contract.ISupportDataSource
import io.wax911.support.extension.util.SupportConnectivityHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import org.koin.core.inject

/**
 * A non paging dependant data source for single resource requests
 *
 * @param parentCoroutineJob parent coroutine from something that is lifecycle aware,
 * this enables us to cancels jobs automatically when the parent is also canceled
 */
abstract class SupportDataSource(
    parentCoroutineJob: Job? = null
) : ISupportDataSource {

    protected val moduleTag: String = javaClass.simpleName

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    override val supervisorJob: Job = SupervisorJob(parentCoroutineJob)

    /**
     * Connectivity helper utility with live data observable capabilities
     */
    override val supportConnectivityHelper by inject<SupportConnectivityHelper>()

    override val networkState = MutableLiveData<NetworkState>()

    protected abstract val databaseHelper: RoomDatabase

    /**
     * Function reference for the retry event
     */
    protected var retry: (() -> Any)? = null

    private fun retryRequestForType() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    /**
     * Handles the requesting data from a the network source and informs the
     * network state that it is in the loading state
     *
     * @param bundle request parameters or more
     */
    open fun startRequestForType(bundle: Bundle) {
        networkState.postValue(NetworkState.LOADING)
        retry = {
            startRequestForType(bundle)
        }
    }

    /**
     * Performs the necessary operation to invoke a network retry request
     */
    override fun retryFailedRequest() {
        retryRequestForType()
    }

    /**
     * Clears all the data in a database table which will assure that
     * and refresh the backing storage medium with new network data
     */
    override fun refreshOrInvalidate() {
        retryRequestForType()
    }
}
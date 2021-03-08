package co.anitrend.arch.data.request

import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.request.queue.RequestQueue
import co.anitrend.arch.data.request.report.RequestStatusReport
import co.anitrend.arch.data.request.wrapper.RequestWrapper
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Contract for request helper
 *
 * @since v1.3.0
 */
abstract class AbstractRequestHelper : IRequestHelper {

    protected val requestQueue: MutableList<RequestQueue> = mutableListOf()

    protected val listeners = CopyOnWriteArrayList<IRequestHelper.Listener>()

    /**
     * Provides a snapshot of status reports for all [requestQueue]
     *
     * @return [RequestStatusReport]
     */
    protected fun prepareStatusReportLocked(request: Request): RequestStatusReport {
        return RequestStatusReport(request)
    }

    /**
     * Adds a new listener that will be notified when any request changes [IRequestHelper.Status].
     *
     * @param listener The listener that will be notified each time a request's status changes.
     * @return True if it is added, false otherwise (e.g. it already exists in the list).
     */
    override fun addListener(
        listener: IRequestHelper.Listener
    ) = listeners.add(listener)

    /**
     * Removes the given listener from the listeners list.
     *
     * @param listener The listener that will be removed.
     * @return True if the listener is removed, false otherwise (e.g. it never existed)
     */
    override fun removeListener(
        listener: IRequestHelper.Listener
    ) = listeners.remove(listener)

    /*
    * Helper extension to dispatch changes to all listeners
    */
    protected fun RequestStatusReport.dispatchReport() {
        listeners.forEach {
            it.onStatusChange(this)
        }
    }
}
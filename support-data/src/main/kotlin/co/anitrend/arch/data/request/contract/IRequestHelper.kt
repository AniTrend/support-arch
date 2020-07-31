package co.anitrend.arch.data.request.contract

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.report.RequestStatusReport
import co.anitrend.arch.data.request.wrapper.RequestWrapper
import co.anitrend.arch.data.request.error.RequestError

/**
 * Request helper contract
 *
 * @since v1.3.0
 */
interface IRequestHelper {

    /**
     * Adds a new listener that will be notified when any request changes [Status].
     *
     * @param listener The listener that will be notified each time a request's status changes.
     * @return True if it is added, false otherwise (e.g. it already exists in the list).
     */
    fun addListener(listener: Listener): Boolean

    /**
     * Removes the given listener from the listeners list.
     *
     * @param listener The listener that will be removed.
     * @return True if the listener is removed, false otherwise (e.g. it never existed)
     */
    fun removeListener(listener: Listener): Boolean

    /**
     * Runs the given [RequestCallback] if no other requests in the given request type is already
     * running. If run, the request will be run in the current thread.
     *
     * @param requestType The type of the request.
     * @param handleCallback The request to run.
     *
     * @return True if the request is run, false otherwise.
     */
    suspend fun runIfNotRunning(
        requestType: RequestType,
        handleCallback: (RequestCallback) -> Unit
    ): Boolean

    /**
     * Records the result of the result of a request
     *
     * @param wrapper Request wrapper delegate
     * @param throwable An optional error
     */
    suspend fun recordResult(
        wrapper: RequestWrapper,
        throwable: RequestError?
    )

    /**
     * Retries all request types for a given [status].
     *
     * @return True if any request is retried, false otherwise.
     */
    suspend fun retryWithStatus(status: Status = Status.FAILED): Boolean

    /**
     * Listener interface to get notified by request status changes.
     */
    interface Listener {
        /**
         * Called when the status for any of the requests has changed.
         *
         * @param report The current status report that has all the information about the requests.
         */
        fun onStatusChange(report: RequestStatusReport)
    }

    /**
     * Available request types.
     */
    enum class RequestType {
        INITIAL,
        BEFORE,
        AFTER
    }

    /**
     * Represents the status of a Request for each [RequestType].
     */
    enum class Status {
        /** There is current a running request. */
        RUNNING,
        /** The last request has succeeded or no such requests have ever been run. */
        SUCCESS,
        /** The last request has failed. */
        FAILED
    }
}
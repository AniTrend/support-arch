package co.anitrend.arch.data.request.helper

import co.anitrend.arch.data.request.*
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.request.error.RequestError
import co.anitrend.arch.data.request.report.RequestStatusReport
import co.anitrend.arch.data.request.wrapper.RequestWrapper
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.CoroutineContext

/**
 * A request helper that manages requests, retrying and blocking duplication
 *
 * @param context The coroutine context that should be used in a coroutine scope
 * @param executor The coroutine context that should be for synchronization
 *
 * @since v1.3.0
 */
class RequestHelper(
    private val context: CoroutineContext,
    private val synchronizer: CoroutineContext
) : AbstractRequestHelper() {

    /**
     * Runs the given [RequestCallback] if no other requests in the given request type is already
     * running. If run, the request will be run in the current thread.
     *
     * @param requestType The type of the request.
     * @param handleCallback The request to run.
     *
     * @return True if the request is run, false otherwise.
     */
    override suspend fun runIfNotRunning(
        requestType: IRequestHelper.RequestType,
        handleCallback: suspend (RequestCallback) -> Unit
    ): Boolean {
        val report = AtomicReference<RequestStatusReport?>(null)

        return withContext(synchronizer) {
            val queue = requestQueues[requestType.ordinal]
            if (queue.running != null)
                return@withContext false
            queue.running = handleCallback
            queue.status = IRequestHelper.Status.RUNNING
            queue.failed = null
            queue.passed = null
            queue.lastError = null

            if (listeners.isNotEmpty())
                report.set(prepareStatusReportLocked())
            report.get()?.dispatchReport()

            RequestWrapper(handleCallback, this@RequestHelper, requestType).invoke(context)
            return@withContext true
        }
    }

    /**
     * Records the result of the result of a request
     *
     * @param wrapper Request wrapper delegate
     * @param throwable An optional error
     */
    override suspend fun recordResult(
        wrapper: RequestWrapper,
        throwable: RequestError?
    ) {
        val report = AtomicReference<RequestStatusReport?>(null)
        val isSuccessful = throwable == null
        withContext(synchronizer) {
            val queue = requestQueues[wrapper.type.ordinal]
            queue.running = null
            queue.lastError = throwable
            if (isSuccessful) {
                queue.failed = null
                queue.passed = wrapper
                queue.status = IRequestHelper.Status.SUCCESS
            } else {
                queue.failed = wrapper
                queue.passed = null
                queue.status = IRequestHelper.Status.FAILED
            }
            if (listeners.isNotEmpty())
                report.set(prepareStatusReportLocked())
        }
        report.get()?.dispatchReport()
    }

    /**
     * Retries all request types for a given [status].
     *
     * @return True if any request is retried, false otherwise.
     */
    override suspend fun retryWithStatus(status: IRequestHelper.Status): Boolean {
        val retried = AtomicBoolean(false)
        withContext(synchronizer) {
            val requestTypes = IRequestHelper.RequestType.values()
            val pendingRetries = arrayOfNulls<RequestWrapper>(requestTypes.size)

            requestTypes.forEach {
                val index = it.ordinal
                val requestQueue = requestQueues[index]
                when (status) {
                    IRequestHelper.Status.FAILED -> {
                        pendingRetries[index] = requestQueue.failed
                        requestQueues[index].failed = null
                    }
                    IRequestHelper.Status.SUCCESS -> {
                        pendingRetries[index] = requestQueue.passed
                        requestQueues[index].passed = null
                    }
                    else -> {
                    }
                }
            }

            pendingRetries
                .filterNotNull()
                .forEach { wrapper ->
                    wrapper.retry()
                    retried.set(true)
                }
        }
        return retried.get()
    }

    /**
     * Check if request handler has any finished request with [status]
     *
     * @return True if a match is found, false otherwise.
     */
    override suspend fun hasAnyWithStatus(status: IRequestHelper.Status): Boolean {
        return withContext(synchronizer) {
            val requestQueue = requestQueues[status.ordinal]
            val requestWrapper = when (status) {
                IRequestHelper.Status.SUCCESS -> requestQueue.passed
                IRequestHelper.Status.FAILED -> requestQueue.failed
                else -> null
            }
            return@withContext requestWrapper != null
        }
    }
}
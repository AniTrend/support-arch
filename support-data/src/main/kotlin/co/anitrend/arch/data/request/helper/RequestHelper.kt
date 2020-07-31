package co.anitrend.arch.data.request.helper

import co.anitrend.arch.data.request.*
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.request.error.RequestError
import co.anitrend.arch.data.request.report.RequestStatusReport
import co.anitrend.arch.data.request.wrapper.RequestWrapper
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.CoroutineContext

/**
 * A request helper that manages requests, retrying and blocking duplication
 *
 * @param context The coroutine context that should be used in a coroutine scope
 *
 * @since v1.3.0
 */
class RequestHelper(
    private val context: CoroutineContext
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
        handleCallback: (RequestCallback) -> Unit
    ): Boolean {
        val hasListeners = !listeners.isEmpty()
        val report = AtomicReference<RequestStatusReport?>(null)
        mutex.withLock {
            val queue = requestQueues[requestType.ordinal]
            if (queue.running != null)
                return false
            queue.running = handleCallback
            queue.status = IRequestHelper.Status.RUNNING
            queue.failed = null
            queue.passed = null
            queue.lastError = null
        }
        if (hasListeners)
            report.set(prepareStatusReportLocked())
        report.get()?.dispatchReport()
        RequestWrapper(
            handleCallback,
            this,
            requestType
        ).invoke(context)
        return true
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
        val hasListeners = !listeners.isEmpty()
        val success = throwable == null
        mutex.withLock {
            val queue = requestQueues[wrapper.type.ordinal]
            queue.running = null
            queue.lastError = throwable
            if (success) {
                queue.failed = null
                queue.passed = wrapper
                queue.status = IRequestHelper.Status.SUCCESS
            } else {
                queue.failed = wrapper
                queue.passed = null
                queue.status = IRequestHelper.Status.FAILED
            }
        }
        if (hasListeners)
            report.set(prepareStatusReportLocked())
        report.get()?.dispatchReport()
    }

    /**
     * Retries all request types for a given [status].
     *
     * @return True if any request is retried, false otherwise.
     */
    override suspend fun retryWithStatus(status: IRequestHelper.Status): Boolean {
        val requestTypes = IRequestHelper.RequestType.values()
        val pendingRetries = arrayOfNulls<RequestWrapper>(requestTypes.size)
        val retried = AtomicBoolean(false)
        mutex.withLock {
            requestTypes.forEach {
                val index = it.ordinal
                when (status) {
                    IRequestHelper.Status.FAILED -> {
                        pendingRetries[index] = requestQueues[index].failed
                        requestQueues[index].failed = null
                    }
                    IRequestHelper.Status.SUCCESS -> {
                        pendingRetries[index] = requestQueues[index].passed
                        requestQueues[index].passed = null
                    }
                    else -> {}
                }
            }
        }
        pendingRetries.forEach {
            it?.also {
                it.retry(context)
                retried.set(true)
            }
        }
        return retried.get()
    }
}
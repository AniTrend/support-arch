package co.anitrend.arch.data.request.helper

import co.anitrend.arch.data.request.*
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.error.RequestError
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.request.queue.RequestQueue
import co.anitrend.arch.data.request.report.RequestStatusReport
import co.anitrend.arch.data.request.wrapper.RequestWrapper
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.CoroutineContext

/**
 * A request helper that manages requests, retrying and blocking duplication
 *
 * @param context The coroutine context that should be used in a coroutine scope
 * @param synchronizer The coroutine context that should be for synchronization
 *
 * @since v1.3.0
 */
class RequestHelper(
    private val context: CoroutineContext,
    private val synchronizer: CoroutineContext
) : AbstractRequestHelper() {

    private fun addOrReuseRequest(request: Request): RequestQueue {
        var queue = requestQueue.firstOrNull { it.request == request }
        if (queue == null) {
            queue = RequestQueue(request)
            requestQueue.add(queue)
        }
        return queue
    }

    /**
     * Runs the given [RequestCallback] if no other requests in the given request type is already
     * running. If run, the request will be run in the current thread.
     *
     * @param request The type of the request.
     * @param handleCallback The request to run.
     *
     * @return True if the request is run, false otherwise.
     */
    override suspend fun runIfNotRunning(
        request: Request,
        handleCallback: suspend (RequestCallback) -> Unit
    ): Boolean {
        val report = AtomicReference<RequestStatusReport?>(null)
        val ran = AtomicBoolean(false)

        withContext(synchronizer) {
            val queue = addOrReuseRequest(request)
            if (queue.running == null) {
                queue.running = handleCallback
                queue.request.status = Request.Status.RUNNING
                queue.request.lastError = null
                queue.failed = null
                queue.passed = null

                if (listeners.isNotEmpty())
                    report.set(prepareStatusReportLocked(request))
                report.get()?.dispatchReport()

                withContext(context) {
                    val wrapper = RequestWrapper(
                        handleCallback = handleCallback,
                        helper = this@RequestHelper,
                        request = request
                    )
                    wrapper.invoke()
                }

                ran.set(true)
            }
        }

        return ran.get()
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
        withContext(synchronizer) {
            val isSuccessful = throwable == null
            var report: RequestStatusReport? = null
            val queue = addOrReuseRequest(wrapper.request)
            queue.running = null
            queue.request.lastError = throwable
            if (isSuccessful) {
                queue.failed = null
                queue.passed = wrapper
                queue.request.status = Request.Status.SUCCESS
            } else {
                queue.failed = wrapper
                queue.passed = null
                queue.request.status = Request.Status.FAILED
            }

            if (listeners.isNotEmpty())
                report = prepareStatusReportLocked(wrapper.request)

            report?.dispatchReport()
        }
    }

    /**
     * Retries all request types for a given [status].
     *
     * @return True if any request is retried, false otherwise.
     */
    override suspend fun retryWithStatus(
        status: Request.Status,
        action: suspend () -> Unit
    ): Boolean {
        val retried = AtomicBoolean(false)
        withContext(synchronizer) {
            val pendingRetries = mutableListOf<RequestWrapper?>()

            requestQueue.forEach { queue ->
                when (status) {
                    Request.Status.SUCCESS -> {
                        pendingRetries.add(queue.passed)
                        queue.passed = null
                    }
                    Request.Status.FAILED -> {
                        pendingRetries.add(queue.failed)
                        queue.failed = null
                    }
                    else -> {}
                }
            }

            if (!pendingRetries.isNullOrEmpty())
                action()

            withContext(context) {
                pendingRetries
                    .forEach { wrapper ->
                        wrapper?.retry()
                        retried.set(true)
                    }
            }
        }
        return retried.get()
    }

    /**
     * Check if request handler has any finished request with [status]
     *
     * @return True if a match is found, false otherwise.
     */
    override suspend fun hasAnyWithStatus(status: Request.Status): Boolean {
        return withContext(synchronizer) {
            val queue = requestQueue.firstOrNull {
                it.request.status == status
            }
            return@withContext queue != null
        }
    }
}
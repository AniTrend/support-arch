/**
 * Copyright 2021 AniTrend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.anitrend.arch.request.helper

import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.AbstractRequestHelper
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.model.Request
import co.anitrend.arch.request.queue.RequestQueue
import co.anitrend.arch.request.report.contract.IRequestStatusReport
import co.anitrend.arch.request.wrapper.RequestWrapper
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

/**
 * A request helper that manages requests, retrying and blocking duplication
 *
 * @param dispatcher Coroutine dispatchers
 *
 * @since v1.3.0
 */
class RequestHelper(
    private val dispatcher: ISupportDispatcher,
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
        handleCallback: suspend (RequestCallback) -> Unit,
    ): Boolean {
        val report = AtomicReference<IRequestStatusReport?>(null)
        val ran = AtomicBoolean(false)

        withContext(dispatcher.confined) {
            val queue = addOrReuseRequest(request)
            if (queue.running == null) {
                queue.running = handleCallback
                queue.request.status = Request.Status.RUNNING
                queue.request.lastError = null
                queue.failed = null
                queue.passed = null

                if (listeners.isNotEmpty()) {
                    report.set(prepareStatusReportLocked(queue.request))
                }
                report.get()?.dispatchReport()

                withContext(dispatcher.io) {
                    val wrapper = RequestWrapper(
                        handleCallback = handleCallback,
                        helper = this@RequestHelper,
                        request = queue.request,
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
        throwable: RequestError?,
    ) {
        withContext(dispatcher.confined) {
            val isSuccessful = throwable == null
            var report: IRequestStatusReport? = null
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

            if (listeners.isNotEmpty()) {
                report = prepareStatusReportLocked(queue.request)
            }

            report?.dispatchReport()
        }
    }

    /**
     * Retries all request types for a given [status].
     *
     * @param status Status for request to retry
     * @param action Action to run when [status is satisfied]
     *
     * @return True if any request is retried, false otherwise.
     */
    override suspend fun retryWithStatus(
        status: Request.Status,
        action: suspend () -> Unit,
    ): Boolean {
        val retried = AtomicBoolean(false)
        withContext(dispatcher.confined) {
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

            if (pendingRetries.isNotEmpty()) {
                action()
            } else {
                Timber.i("No requests for status: $status to retry")
            }

            withContext(dispatcher.io) {
                pendingRetries.filterNotNull()
                    .forEach { wrapper ->
                        wrapper.retry()
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
        return withContext(dispatcher.confined) {
            val queue = requestQueue.firstOrNull {
                it.request.status == status
            }
            return@withContext queue != null
        }
    }
}

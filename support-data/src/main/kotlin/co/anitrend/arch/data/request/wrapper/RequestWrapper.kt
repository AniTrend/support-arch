package co.anitrend.arch.data.request.wrapper

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.request.model.Request

/**
 * A wrapper for creating requests
 *
 * @param handleCallback The callback that should be invoked
 * @param helper A type of request helper to use
 * @param request The type of request
 *
 * @since v1.3.0
 */
class RequestWrapper internal constructor(
    val handleCallback: suspend (RequestCallback) -> Unit,
    val helper: IRequestHelper,
    val request: Request
) {
    /**
     * Retries a request if it is not already running
     */
    suspend operator fun invoke() {
        handleCallback(
            RequestCallback(
                this@RequestWrapper,
                helper
            )
        )
    }

    /**
     * Retries a request if it is not already running
     */
    suspend fun retry() {
        helper.runIfNotRunning(request, handleCallback)
    }
}
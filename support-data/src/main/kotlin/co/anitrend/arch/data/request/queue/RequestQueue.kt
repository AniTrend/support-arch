package co.anitrend.arch.data.request.queue

import co.anitrend.arch.data.request.wrapper.RequestWrapper
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.model.Request

/**
 * Request queue model
 *
 * @since v1.3.0
 */
data class RequestQueue(val request: Request) {
    var failed: RequestWrapper? = null
    var passed: RequestWrapper? = null
    var running: (suspend (RequestCallback) -> Unit)? = null
}
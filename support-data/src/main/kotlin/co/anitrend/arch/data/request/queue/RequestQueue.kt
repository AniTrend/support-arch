package co.anitrend.arch.data.request.queue

import co.anitrend.arch.data.request.wrapper.RequestWrapper
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.request.error.RequestError

/**
 * Request queue model
 *
 * @since v1.3.0
 */
internal class RequestQueue(
    val requestType: IRequestHelper.RequestType
) {
    var failed: RequestWrapper? = null
    var passed: RequestWrapper? = null
    var running: ((RequestCallback) -> Unit)? = null
    var lastError: RequestError? = null
    var status = IRequestHelper.Status.SUCCESS
}
package co.anitrend.arch.data.request.callback

import co.anitrend.arch.data.request.wrapper.RequestWrapper
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.request.error.RequestError
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A request callback for updating request status to the [helper]
 *
 * @param wrapper The wrapper that was executed
 * @param helper The helper to report to
 *
 * @since v1.3.0
 */
class RequestCallback(
    private val wrapper: RequestWrapper,
    private val helper: IRequestHelper
) {
    private val called = AtomicBoolean()

    /**
     * Call this method when the request succeeds and new data is fetched.
     *
     * @throws IllegalStateException when [recordFailure]/[recordSuccess] has already been called
     */
    @Throws(IllegalStateException::class)
    suspend fun recordSuccess() {
        if (called.compareAndSet(false, true)) {
            helper.recordResult(wrapper, null)
        } else {
            throw IllegalStateException(
                "already called recordSuccess or recordFailure"
            )
        }
    }

    /**
     * Call this method with the failure message and the request can be retried via
     * [IRequestHelper.retryWithStatus].
     *
     * @param throwable The error that occurred while carrying out the request.
     *
     * @throws IllegalStateException when [recordFailure]/[recordSuccess] has already been called
     */
    @Throws(IllegalStateException::class)
    suspend fun recordFailure(throwable: RequestError) {
        if (called.compareAndSet(false, true)) {
            helper.recordResult(wrapper, throwable)
        } else {
            throw IllegalStateException(
                "already called recordSuccess or recordFailure"
            )
        }
    }
}
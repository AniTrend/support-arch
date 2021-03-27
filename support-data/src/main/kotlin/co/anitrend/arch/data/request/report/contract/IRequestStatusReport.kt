package co.anitrend.arch.data.request.report.contract

import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.domain.entities.RequestError

/**
 * Status report contract
 */
interface IRequestStatusReport {

    /**
     * Convenience method to check if there are any running requests.
     *
     * @return True if there are any running requests, false otherwise.
     */
    fun hasRunning(): Boolean

    /**
     * Convenience method to check if there are any requests that resulted in an error.
     *
     * @return True if there are any requests that finished with error, false otherwise.
     */
    fun hasError(): Boolean

    /**
     * Convenience method to check if there are any idle requests.
     *
     * @return True if there are no requests.
     */
    fun hasIdle(): Boolean

    /**
     * Convenience method to check if there are any requests that resulted in success.
     *
     * @return True if there are any requests that finished without an error, false otherwise.
     */
    fun hasSuccess(): Boolean

    /**
     * Returns the error for the given request type.
     *
     * @param type The request type for which the error should be returned.
     *
     * @return The [Throwable] returned by the failing request with the given type or
     * `null` if the request for the given type did not fail.
     */
    fun getErrorFor(type: Request.Type): RequestError?

    /**
     * Returns the current request type.
     *
     * @return The [Request.Type] in the current context
     */
    fun getType(): Request.Type
}
package co.anitrend.arch.data.request.report

import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.arch.data.request.model.Request

/**
 * Data class that holds the information about the current status of the ongoing requests
 * using this helper.
 */
data class RequestStatusReport internal constructor(
    private val request: Request
) {

    /**
     * Convenience method to check if there are any running requests.
     *
     * @return True if there are any running requests, false otherwise.
     */
    fun hasRunning(): Boolean {
        return request.status == Request.Status.RUNNING
    }

    /**
     * Convenience method to check if there are any requests that resulted in an error.
     *
     * @return True if there are any requests that finished with error, false otherwise.
     */
    fun hasError(): Boolean {
        return request.status == Request.Status.FAILED
    }

    /**
     * Convenience method to check if there are any idle requests.
     *
     * @return True if there are no requests.
     */
    fun hasIdle(): Boolean {
        return request.status == Request.Status.IDLE
    }

    /**
     * Convenience method to check if there are any requests that resulted in success.
     *
     * @return True if there are any requests that finished without an error, false otherwise.
     */
    fun hasSuccess(): Boolean {
        return request.status == Request.Status.SUCCESS
    }

    /**
     * Returns the error for the given request type.
     *
     * @param type The request type for which the error should be returned.
     *
     * @return The [Throwable] returned by the failing request with the given type or
     * `null` if the request for the given type did not fail.
     */
    fun getErrorFor(type: Request.Type): RequestError? {
        if (request.type == type)
            return request.lastError
        return null
    }

    /**
     * Returns the current request type.
     *
     * @return The [Request.Type] in the current context
     */
    fun getType(): Request.Type = request.type

    override fun toString(): String {
        return """
                StatusReport {
                    request: $request
                }
            """.trimIndent()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is RequestStatusReport ->
                request == other.request
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return request.hashCode()
    }
}
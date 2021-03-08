package co.anitrend.arch.data.request.report

import co.anitrend.arch.data.request.error.RequestError
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
package co.anitrend.arch.data.request.report

import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.request.error.RequestError

/**
 * Data class that holds the information about the current status of the ongoing requests
 * using this helper.
 *
 * @param initial Status of the latest request that were submitted with [IRequestHelper.RequestType.INITIAL].
 * @param before Status of the latest request that were submitted with [IRequestHelper.RequestType.BEFORE].
 * @param after Status of the latest request that were submitted with [IRequestHelper.RequestType.AFTER].
 */
data class RequestStatusReport internal constructor(
    private val initial: IRequestHelper.Status,
    private val before: IRequestHelper.Status,
    private val after: IRequestHelper.Status,
    private val errors: Array<RequestError?>
) {

    /**
     * Convenience method to check if there are any running requests.
     *
     * @return True if there are any running requests, false otherwise.
     */
    fun hasRunning(): Boolean {
        return initial == IRequestHelper.Status.RUNNING ||
                before == IRequestHelper.Status.RUNNING ||
                after == IRequestHelper.Status.RUNNING
    }

    /**
     * Convenience method to check if there are any requests that resulted in an error.
     *
     * @return True if there are any requests that finished with error, false otherwise.
     */
    fun hasError(): Boolean {
        return initial == IRequestHelper.Status.FAILED ||
                before == IRequestHelper.Status.FAILED ||
                after == IRequestHelper.Status.FAILED
    }

    /**
     * Returns the error for the given request type.
     *
     * @param type The request type for which the error should be returned.
     *
     * @return The [Throwable] returned by the failing request with the given type or
     * `null` if the request for the given type did not fail.
     */
    fun getErrorFor(type: IRequestHelper.RequestType): RequestError? {
        return errors[type.ordinal]
    }

    override fun toString(): String {
        return """
                StatusReport {
                    initial: $initial
                    before: $before
                    after: $after
                    errors: ${errors.contentToString()}
                }
            """.trimIndent()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as RequestStatusReport
        if (initial != that.initial) return false
        if (before != that.before) return false
        return if (after != that.after) false else errors.contentEquals(that.errors)
    }

    override fun hashCode(): Int {
        var result = initial.hashCode()
        result = 31 * result + before.hashCode()
        result = 31 * result + after.hashCode()
        result = 31 * result + errors.contentHashCode()
        return result
    }
}
package co.anitrend.arch.data.request.error

/**
 * Custom error for requests
 *
 * @param topic Summary of the error
 * @param description Description of the error
 * @param throwable Full stack cause of the error
 *
 * @since v1.3.0
 */
data class RequestError(
    val topic: String?,
    val description: String?,
    val throwable: Throwable?
) : Throwable(description, throwable) {
    constructor(cause: Throwable?) : this(null, cause?.toString(), cause)
    constructor() : this(null, null, null)
}
package co.anitrend.arch.domain.entities

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
    val topic: String? = null,
    val description: String? = null,
    val throwable: Throwable? = null
) : Throwable(description, throwable) {
    constructor(cause: Throwable?) : this(
        cause?.javaClass?.simpleName,
        cause?.toString(),
        cause
    )
}
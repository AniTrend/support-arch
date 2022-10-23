package co.anitrend.arch.data.request.exception

sealed class RequestException(
    message: String? = null,
    cause: Throwable? = null
) : IllegalStateException(message, cause) {

    class ResultAlreadyRecorded(
        override val message: String? = null,
        override val cause: Throwable? = null
    ) : RequestException()
}

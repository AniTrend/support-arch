package io.wax911.support.core.wrapper.extension

import io.wax911.support.core.wrapper.RequestResult

fun RequestResult<*>.isSuccessful() = code in 200..226
fun RequestResult<*>.isRedirect() = code in 300..308

fun RequestResult<*>.isBadRequest() = code == 400
fun RequestResult<*>.isUnauthorized() = code == 401
fun RequestResult<*>.isPaymentRequired() = code == 402
fun RequestResult<*>.isForbidden() = code == 403
fun RequestResult<*>.isNotFound() = code == 404
fun RequestResult<*>.isRateLimitReached() = code == 429

fun RequestResult<*>.isServerError() = code in 500..599
fun RequestResult<*>.isRequestFailure() = code == null
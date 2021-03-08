package co.anitrend.arch.domain.extensions

import co.anitrend.arch.domain.entities.NetworkState

/**
 * @return True if the network state is loading
 */
fun NetworkState.isLoading() = this is NetworkState.Loading

/**
 * @return True if the network state is success
 */
fun NetworkState.isSuccess() = this is NetworkState.Success

/**
* @return True if the network state is an error
*/
fun NetworkState.isError() = this is NetworkState.Error

/**
 * @return True if the network state is needs to redirect
 */
fun NetworkState.isRedirect() = this is NetworkState.Error && code in 300..308

/**
 * @return True if the network state is a bad request
 */
fun NetworkState.isBadRequest() = this is NetworkState.Error && code == 400

/**
 * @return True if the network state requires authorization
 */
fun NetworkState.isUnauthorized() = this is NetworkState.Error && code == 401

/**
 * @return True if the network state is a required payment
 */
fun NetworkState.isPaymentRequired() = this is NetworkState.Error && code == 402

/**
 * @return True if the network state is forbidden
 */
fun NetworkState.isForbidden() = this is NetworkState.Error && code == 403

/**
 * @return True if the network state is not found
 */
fun NetworkState.isNotFound() = this is NetworkState.Error && code == 404

/**
 * @return True if the network state is rate limited
 */
fun NetworkState.isRateLimitReached() = this is NetworkState.Error && code == 429

/**
 * @return True if the network state is a server error
 */
fun NetworkState.isServerError() = this is NetworkState.Error && code in 500..599

/**
 * @return True if the network has an undefined error code
 */
fun NetworkState.isWTF() = this is NetworkState.Error && code == null
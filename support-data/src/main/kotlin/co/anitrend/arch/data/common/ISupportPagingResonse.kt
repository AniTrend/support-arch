package co.anitrend.arch.data.common

import androidx.paging.PagingRequestHelper

/**
 * Contract response handler for wrapping future requests, commonly in the form of a
 * [kotlinx.coroutines.Deferred] or [retrofit2.Call]
 *
 * This focuses on handling paging requests using [androidx.paging.PagingRequestHelper]
 *
 * @since v1.2.0
 */
interface ISupportPagingResonse<in RESOURCE> {

    /**
     * Response handler for coroutine contexts, mainly for paging
     *
     * @param resource awaiting execution
     * @param pagingRequestHelper optional paging request callback
     */
    suspend operator fun invoke(
        resource: RESOURCE,
        pagingRequestHelper: PagingRequestHelper.Request.Callback
    )
}
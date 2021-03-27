package co.anitrend.arch.extension.util.pagination

import co.anitrend.arch.extension.util.pagination.contract.ISupportPagingHelper

/**
 * Paging helper, makes the manipulating of paging related variables easier
 *
 * @since v1.1.0
 */
data class SupportPagingHelper(
    var page: Int = 1,
    var pageOffset: Int = 0,
    val pageSize: Int,
    var isPagingLimit: Boolean
) : ISupportPagingHelper {

    /**
     * Resets the paging parameters to their default
     */
    override fun onPageRefresh() {
        page = 1
        pageOffset = 0
        isPagingLimit = false
    }

    /**
     * Calculates the previous page offset and index
     */
    override fun onPagePrevious() {
        page -= 1
        pageOffset -= pageSize
    }

    /**
     * Calculates the next page offset and index
     */
    override fun onPageNext() {
        page += 1
        pageOffset += pageSize
    }

    /**
     * Checks if the current page and offset is the first
     */
    override fun isFirstPage() =
        page == 1 && pageOffset == 0
}
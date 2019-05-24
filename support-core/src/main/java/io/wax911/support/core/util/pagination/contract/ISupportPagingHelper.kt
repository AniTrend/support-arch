package io.wax911.support.core.util.pagination.contract

interface ISupportPagingHelper {

    /**
     * Resets the paging parameters to their default
     */
    fun onPageRefresh()

    /**
     * Calculates the next page offset and index
     */
    fun onPageNext()

    /**
     * Checks if the current page and offset is the first
     */
    fun isFirstPage(): Boolean
}
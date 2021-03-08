package co.anitrend.arch.extension.util.pagination.contract

/**
 * Contract for paging helper
 *
 * @since v1.1.0
 */
interface ISupportPagingHelper {

    /**
     * Resets the paging parameters to their default
     */
    fun onPageRefresh()

    /**
     * Calculates the previous page offset and index
     */
    fun onPagePrevious()

    /**
     * Calculates the next page offset and index
     */
    fun onPageNext()

    /**
     * Checks if the current page and offset is the first
     */
    fun isFirstPage(): Boolean
}
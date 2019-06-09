package io.wax911.support.data.util.pagination

import android.os.Parcelable
import io.wax911.support.data.util.pagination.contract.ISupportPagingHelper
import kotlinx.android.parcel.Parcelize

/**
 * Paging helper, makes the manipulating of paging related variables easier
 */
@Parcelize
data class SupportPagingHelper(
    var page: Int = 1,
    var pageOffset: Int = 0,
    val pageSize: Int,
    var isPagingLimit: Boolean
) : Parcelable, ISupportPagingHelper {

    /**
     * Resets the paging parameters to their default
     */
    override fun onPageRefresh() {
        page = 1
        pageOffset = 0
        isPagingLimit = false
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

    fun fromBundle(pagingHelper: SupportPagingHelper?) {
        pagingHelper?.also {
            page = it.page
            pageOffset = it.pageOffset
            isPagingLimit = it.isPagingLimit
        }
    }
}
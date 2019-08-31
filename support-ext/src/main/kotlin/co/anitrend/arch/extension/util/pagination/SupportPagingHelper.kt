package co.anitrend.arch.extension.util.pagination

import android.os.Bundle
import android.os.Parcelable
import co.anitrend.arch.extension.util.SupportExtKeyStore
import co.anitrend.arch.extension.util.pagination.contract.ISupportPagingHelper
import kotlinx.android.parcel.Parcelize

/**
 * Paging helper, makes the manipulating of paging related variables easier
 *
 * @since v1.1.0
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

    /**
     * Restore state from bundle
     *
     * @param bundle saved state
     */
    fun from(bundle: Bundle?) {
        val saved = bundle?.getParcelable<SupportPagingHelper>(
            SupportExtKeyStore.key_pagination
        )
        if (saved != null) {
            page = saved.page
            pageOffset = saved.pageOffset
            isPagingLimit = saved.isPagingLimit
        }
    }
}
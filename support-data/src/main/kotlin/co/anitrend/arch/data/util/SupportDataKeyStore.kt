package co.anitrend.arch.data.util

import androidx.paging.PagedList
import co.anitrend.arch.extension.util.SupportExtKeyStore.pagingLimit

/**
 * Shared constant value holder
 *
 * @since v1.1.0
 */
object SupportDataKeyStore {

    val PAGING_CONFIGURATION = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(pagingLimit)
        .setPrefetchDistance(15)
        .setMaxSize(75)
        .build()
}
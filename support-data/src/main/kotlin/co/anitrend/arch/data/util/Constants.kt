package co.anitrend.arch.data.util

import androidx.paging.PagedList
import co.anitrend.arch.extension.util.SupportExtKeyStore.pagingLimit


/**
 * Default paging configuration
 */
val PAGING_CONFIGURATION = PagedList.Config.Builder()
    .setEnablePlaceholders(true)
    .setPageSize(pagingLimit)
    .setPrefetchDistance(15)
    .setMaxSize(75)
    .build()

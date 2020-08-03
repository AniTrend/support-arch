package co.anitrend.arch.data.util

import androidx.paging.PagedList
import co.anitrend.arch.extension.util.DEFAULT_PAGE_SIZE


/**
 * Default paging configuration
 */
val PAGING_CONFIGURATION = PagedList.Config.Builder()
    .setEnablePlaceholders(true)
    .setPageSize(DEFAULT_PAGE_SIZE)
    .setPrefetchDistance(15)
    .setMaxSize(75)
    .build()

package io.wax911.sample.core.util

import androidx.paging.PagedList

object StateUtil  {

    /** Default Values  */
    const val aspectRatio = 4/3f
    const val wideAspectRatio = 16/9f
    const val peekHeight = 200f
    const val pagingLimit = 21
    const val glideTimeOut = 10000
    const val singleItemLimit = 1

    /** Main Application Args  */
    const val arg_page = "arg_page"
    const val arg_title = "arg_title"
    const val arg_model = "arg_model"
    const val arg_redirect = "arg_redirect"
    const val arg_request_type = "arg_request_type"
    const val arg_shortcut_used = "arg_shortcut_used"
    const val arg_selection_mode = "arg_selection_mode"


    const val authorization = "Authorization"

    val PAGING_CONFIGURATION = PagedList.Config.Builder()
        .setMaxSize(pagingLimit * 2)
        .setPageSize(pagingLimit)
        .setEnablePlaceholders(false)
        .build()
}

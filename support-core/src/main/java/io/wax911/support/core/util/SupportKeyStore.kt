package io.wax911.support.core.util

import androidx.paging.PagedList

object SupportKeyStore {

    const val key_navigation_selected = "key_navigation_selected"
    const val key_navigation_title = "key_navigation_title"
    const val key_pagination = "key_pagination"
    const val key_arg_bundle = "key_arg_bundle"


    const val arg_title = "arg_title"
    const val arg_redirect = "arg_redirect"
    const val arg_request_type = "arg_request_type"
    const val arg_shortcut_used = "arg_shortcut_used"
    const val arg_selection_mode = "arg_selection_mode"

    const val peekHeight = 200f
    const val pagingLimit = 21
    const val glideTimeOut = 10000

    val PAGING_CONFIGURATION = PagedList.Config.Builder()
        .setPageSize(pagingLimit)
        .setEnablePlaceholders(false)
        .build()
}
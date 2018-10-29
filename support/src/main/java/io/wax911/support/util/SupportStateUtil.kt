package io.wax911.support.util

import androidx.annotation.IntDef

interface SupportStateUtil {
    companion object {

        // Application State Keys
        val key_navigation_selected = "key_navigation_selected"
        val key_navigation_title = "key_navigation_title"
        val key_pagination_limit = "key_pagination_limit"
        val key_recycler_state = "key_recycler_state"
        val key_bundle_param = "key_bundle_param"
        val key_model_state = "key_model_state"
        val key_pagination = "key_pagination"
        val key_columns = "key_columns"
        val arg_order = "order"

        // Common Arguments
        val arg_page_offset = "arg_page_offset"
        val arg_page_limit = "arg_page_limit"
        val arg_page = "arg_page"

        val arg_positive_text = "arg_positive_text"
        val arg_negative_text = "arg_negative_text"

        val arg_bundle = "arg_bundle"

        const val RECYCLER_TYPE_CONTENT = 0x00000010
        const val RECYCLER_TYPE_HEADER = 0x00000011
        const val RECYCLER_TYPE_LOADING = 0x00000100
        const val RECYCLER_TYPE_EMPTY = 0x00000101
        const val RECYCLER_TYPE_ERROR = 0x00000110

        @IntDef(RECYCLER_TYPE_CONTENT, RECYCLER_TYPE_HEADER,
                RECYCLER_TYPE_LOADING, RECYCLER_TYPE_EMPTY,
                RECYCLER_TYPE_ERROR)
        annotation class RecyclerViewType
    }
}

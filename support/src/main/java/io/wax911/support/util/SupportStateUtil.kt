package io.wax911.support.util

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
    }
}

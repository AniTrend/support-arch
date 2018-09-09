package io.wax911.sample.util

import io.wax911.support.util.SupportStateUtil

interface StateUtil : SupportStateUtil {
    companion object {

        /** Default Values  */
        val AspectRatio = 1.37f
        val WideAspectRatio = 0.95f
        val PEEK_HEIGHT = 200f
        val PAGING_LIMIT = 21
        val GLIDE_REQUEST_TIMEOUT = 10000
        val SINGLE_ITEM_LIMIT = 1

        /** Base Application Args  */
        val arg_feed = "arg_feed"
        val arg_title = "arg_title"
        val arg_model = "arg_model"
        val arg_popular = "arg_popular"
        val arg_redirect = "arg_redirect"
        val arg_user_model = "arg_user_model"
        val arg_list_model = "arg_list_model"
        val arg_branch_name = "arg_branch_name"
        val arg_request_type = "arg_request_type"
        val arg_activity_tag = "arg_activity_tag"
        val arg_message_type = "arg_message_type"
        val arg_shortcut_used = "arg_shortcut_used"
        val arg_deep_link_type = "arg_deep_link_type"
    }
}

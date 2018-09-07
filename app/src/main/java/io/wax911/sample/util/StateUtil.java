package io.wax911.sample.util;

public @interface StateUtil {

    /** Default Values */
    float AspectRatio = 1.37f;
    float WideAspectRatio = 0.95f;
    float PEEK_HEIGHT = 200f;
    int PAGING_LIMIT = 21, GLIDE_REQUEST_TIMEOUT = 10000, SINGLE_ITEM_LIMIT = 1;

    /** Base Application Args */
    String arg_feed = "arg_feed";
    String arg_title = "arg_title";
    String arg_model = "arg_model";
    String arg_popular = "arg_popular";
    String arg_redirect = "arg_redirect";
    String arg_user_model = "arg_user_model";
    String arg_list_model = "arg_list_model";
    String arg_branch_name = "arg_branch_name";
    String arg_page_offset = "arg_page_offset";
    String arg_request_type = "arg_request_type";
    String arg_activity_tag = "arg_activity_tag";
    String arg_message_type = "arg_message_type";
    String arg_shortcut_used = "arg_shortcut_used";
    String arg_deep_link_type = "arg_deep_link_type";

    String arg_positive_text = "arg_positive_text";
    String arg_negative_text = "arg_negative_text";

    /** Application State Keys */

    String arg_order = "order";
    String key_recycler_state = "key_recycler_state";
    String key_model_state = "key_model_state";
    String key_pagination = "key_pagination";
    String key_columns = "key_columns";
    String key_navigation_selected = "key_navigation_selected";
    String key_navigation_title = "key_navigation_title";
    String key_bundle_param = "key_bundle_param";
}

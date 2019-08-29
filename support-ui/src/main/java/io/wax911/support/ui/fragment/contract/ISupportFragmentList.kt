package io.wax911.support.ui.fragment.contract

import androidx.annotation.IntegerRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.paging.PagedList
import io.wax911.support.ui.R

/**
 *
 * @since 0.9.X
 */
interface ISupportFragmentList<M> {

    @get:StringRes
    val retryButtonText: Int

    @get:StringRes
    val loadingMessage: Int
        get() = R.string.supportTextLoading

    @get:IntegerRes
    val columnSize: Int
        get() = R.integer.grid_list_x3


    @get:LayoutRes
    val inflateLayout: Int
        get() =  R.layout.support_list


    /**
     * Handles post view model result after extraction or processing
     *
     * @param pagedList paged list holding data
     */
    fun onPostModelChange(pagedList: PagedList<M>?)
}
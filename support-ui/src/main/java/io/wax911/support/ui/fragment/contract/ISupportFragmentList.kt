package io.wax911.support.ui.fragment.contract

import androidx.annotation.IntegerRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.paging.PagedList
import io.wax911.support.data.model.contract.IUiModel
import io.wax911.support.ui.R

interface ISupportFragmentList<M> {

    val retryButtonText: Int
        @StringRes
        get

    val loadingMessage: Int
        @StringRes
        get() = R.string.supportTextLoading

    val columnSize: Int
        @IntegerRes
        get() = R.integer.grid_list_x3

    val inflateLayout: Int
        @LayoutRes
        get() =  R.layout.support_list

    /**
     * Sets up the [io.wax911.support.ui.recycler.SupportRecyclerView] with
     * [io.wax911.support.ui.recycler.adapter.SupportViewAdapter]
     * and additional properties if needed, after it will change the state layout to empty or content.
     */
    fun injectAdapter()

    /**
     * Handles post view model result after extraction or processing
     *
     * @param pagedList paged list holding data
     */
    fun onPostModelChange(pagedList: PagedList<M>?)
}
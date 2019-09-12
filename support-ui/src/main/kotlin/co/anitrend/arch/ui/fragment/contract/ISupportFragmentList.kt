package co.anitrend.arch.ui.fragment.contract

import androidx.annotation.IntegerRes
import androidx.annotation.LayoutRes
import androidx.paging.PagedList
import co.anitrend.arch.ui.R

/**
 * Fragment list contract
 *
 * @since v0.9.X
 */
interface ISupportFragmentList<M> {

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
package co.anitrend.arch.ui.fragment.contract

import androidx.annotation.IntegerRes
import androidx.annotation.LayoutRes
import androidx.paging.PagedList
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.view.widget.SupportStateLayout

/**
 * Fragment list contract
 *
 * @since v0.9.X
 */
interface ISupportFragmentList<M> : SwipeRefreshLayout.OnRefreshListener {

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

    /**
     * Informs the underlying [SupportStateLayout] of changes to the [NetworkState]
     *
     * @param networkState New state from the application
     */
    fun changeLayoutState(networkState: NetworkState?)
}
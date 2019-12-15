package co.anitrend.arch.ui.fragment.contract

import android.view.View
import androidx.annotation.IntegerRes
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.recycler.SupportRecyclerView
import co.anitrend.arch.ui.recycler.adapter.contract.ISupportViewAdapter
import co.anitrend.arch.ui.util.SupportStateLayoutConfiguration
import co.anitrend.arch.ui.view.widget.SupportStateLayout

/**
 * Fragment list contract
 *
 * @since v0.9.X
 */
interface ISupportFragmentList<M> : SwipeRefreshLayout.OnRefreshListener {

    var supportStateLayout: SupportStateLayout?
    var supportRefreshLayout: SwipeRefreshLayout?
    var supportRecyclerView: SupportRecyclerView?

    /**
     * Default grid sized items for the built in layout manager
     *
     * @see R.integer.grid_list_x3
     */
    @get:IntegerRes
    val columnSize: Int
        get() = R.integer.grid_list_x3

    val stateLayoutOnClick: View.OnClickListener

    val adapterFooterRetryAction: View.OnClickListener

    val onRefreshObserver: Observer<NetworkState>

    val onNetworkObserver: Observer<NetworkState>

    /**
     * Adapter that should be used for the recycler view
     */
    val supportViewAdapter: ISupportViewAdapter<M>

    /**
     * State configuration for any underlying state representing widgets
     */
    val supportStateConfiguration: SupportStateLayoutConfiguration

    /**
     * Sets a layout manager to the recycler view
     */
    fun setRecyclerLayoutManager(recyclerView: SupportRecyclerView)

    /**
     * Sets the adapter for the recycler view
     */
    fun setRecyclerAdapter(recyclerView: SupportRecyclerView)

    /**
     * Informs the underlying [SupportStateLayout] of changes to the [NetworkState]
     *
     * @param networkState New state from the application
     */
    fun changeLayoutState(networkState: NetworkState?)
}
package co.anitrend.arch.ui.fragment.contract

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.recycler.SupportRecyclerView
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.ui.view.widget.SupportStateLayout
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig

/**
 * Fragment list contract
 *
 * @since v0.9.X
 */
interface ISupportFragmentList<T> : SwipeRefreshLayout.OnRefreshListener {

    val onRefreshObserver: Observer<NetworkState>
    val onNetworkObserver: Observer<NetworkState>

    /**
     * Adapter that should be used for the recycler view, by default [StateRestorationPolicy]
     * is set to [StateRestorationPolicy.PREVENT_WHEN_EMPTY]
     */
    val supportViewAdapter: ISupportAdapter<T>

    /**
     * State configuration for any underlying state representing widgets
     */
    val stateConfig: StateLayoutConfig

    /**
     * Provides a layout manager that should be used by [setRecyclerLayoutManager]
     */
    fun provideLayoutManager(): RecyclerView.LayoutManager

    /**
     * Sets a layout manager to the recycler view
     *
     * @see provideLayoutManager
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
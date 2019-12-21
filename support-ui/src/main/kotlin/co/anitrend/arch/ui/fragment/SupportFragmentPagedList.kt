package co.anitrend.arch.ui.fragment

import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.anitrend.arch.core.presenter.SupportPresenter
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.ui.fragment.contract.ISupportFragmentList
import co.anitrend.arch.ui.recycler.SupportRecyclerView
import co.anitrend.arch.ui.recycler.adapter.SupportPagedListAdapter

/**
 * Core implementation for fragments that rely on pagination data sets using [PagedList]
 *
 * @since v0.9.X
 * @see SupportFragment
 * @see ISupportFragmentList
 */
abstract class SupportFragmentPagedList<M, P : SupportPresenter<*>, VM> : SupportFragmentList<M, P, VM>() {

    /**
     * Sets the adapter for the recycler view
     */
    override fun setRecyclerAdapter(recyclerView: SupportRecyclerView) {
        if (recyclerView.adapter == null) {
            recyclerView.adapter = supportViewAdapter.let {
                if (it.supportAction == null)
                    it.supportAction = supportAction

                it as SupportPagedListAdapter<M>
            }
        }
    }

    /**
     * Handles post view model result after extraction or processing
     *
     * @param model paged list holding data
     */
    fun onPostModelChange(model: PagedList<M>?) {
        with (supportViewAdapter as SupportPagedListAdapter<M>) {
            submitList(model) /*{
                // Workaround for an issue where RecyclerView incorrectly uses the loading / spinner
                // item added to the end of the list as an anchor during initial load.
                val layoutManager = (supportRecyclerView?.layoutManager as StaggeredGridLayoutManager)
                val position = layoutManager.findFirstCompletelyVisibleItemPositions(null)
                if (position.first() != RecyclerView.NO_POSITION) {
                    supportRecyclerView?.scrollToPosition(position.first())
                }
            }*/
        }

        if (!model.isNullOrEmpty())
            supportStateLayout?.setNetworkState(NetworkState.Success)
        else {
            if (supportViewAdapter.hasExtraRow()) {
                supportStateLayout?.setNetworkState(NetworkState.Success)
                supportViewAdapter.networkState = NetworkState.Loading
            }
            /*else
                supportStateLayout?.setNetworkState(NetworkState.Loading)*/
        }

        onUpdateUserInterface()
        resetWidgetStates()
    }
}
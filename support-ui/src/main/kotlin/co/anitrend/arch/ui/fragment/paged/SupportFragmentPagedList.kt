package co.anitrend.arch.ui.fragment.paged

import androidx.paging.PagedList
import co.anitrend.arch.core.presenter.SupportPresenter
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.ui.fragment.contract.ISupportFragmentList
import co.anitrend.arch.ui.fragment.list.SupportFragmentList
import co.anitrend.arch.ui.recycler.SupportRecyclerView
import co.anitrend.arch.ui.recycler.adapter.SupportPagedListAdapter

/**
 * Core implementation for fragments that rely on pagination data sets using [PagedList]
 *
 * @since v0.9.X
 *
 * @see ISupportFragmentList
 */
abstract class SupportFragmentPagedList<M> : SupportFragmentList<M>() {

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
            submitList(model)
        }

        if (!model.isNullOrEmpty())
            supportStateLayout?.setNetworkState(NetworkState.Success)
        else if (supportViewAdapter.hasExtraRow()) {
            supportStateLayout?.setNetworkState(NetworkState.Success)
            supportViewAdapter.networkState = NetworkState.Loading
        }


        onUpdateUserInterface()
        resetWidgetStates()
    }
}
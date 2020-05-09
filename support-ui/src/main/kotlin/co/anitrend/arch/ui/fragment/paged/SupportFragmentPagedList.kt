package co.anitrend.arch.ui.fragment.paged

import androidx.paging.PagedList
import co.anitrend.arch.recycler.adapter.SupportPagedListAdapter
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.ui.fragment.contract.ISupportFragmentList
import co.anitrend.arch.ui.fragment.list.SupportFragmentList

/**
 * Core implementation for fragments that rely on pagination data sets using [PagedList]
 *
 * @since v0.9.X
 *
 * @see ISupportFragmentList
 */
abstract class SupportFragmentPagedList<M> : SupportFragmentList<M>() {

    /**
     * Handles post view model result after extraction or processing
     *
     * @param model paged list holding data
     */
    override fun onPostModelChange(model: Collection<M>?) {
        with (supportViewAdapter as SupportPagedListAdapter) {
            model as PagedList
            submitList(model)
        }

        afterPostModelChange(model)
    }
}
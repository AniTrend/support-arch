package co.anitrend.arch.recycler.action

import co.anitrend.arch.core.presenter.contract.ISupportPresenter
import co.anitrend.arch.recycler.action.contract.ISupportSelectionListener
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.model.RecyclerItem

/**
 * A helper class for triggering action mode for
 * [co.anitrend.arch.recycler.holder.SupportViewHolder]
 *
 * @since v1.3.0
 */
class SupportSelectionMode<I: RecyclerItem>(
    private val selectionListener: ISupportSelectionListener?,
    private val presenter: ISupportPresenter?
) : ISupportSelectionMode<I> {

    /**
     * Clears all selected items in the current context and alternative stop the current action mode
     */
    override fun clearSelection() {
        TODO("Not yet implemented")
    }

    /**
     * Checks if item exists in the current selection
     */
    override fun containsItem(item: I?): Boolean {
        TODO("Not yet implemented")
    }
}
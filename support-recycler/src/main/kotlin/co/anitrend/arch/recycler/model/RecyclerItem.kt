package co.anitrend.arch.recycler.model

import android.view.View
import co.anitrend.arch.recycler.action.decorator.ISelectionDecorator
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.contract.IRecyclerItem

/**
 * A wrapper for items that can be displayed inside the recycler view
 *
 * @since v1.3.0
 *
 * @see IRecyclerItem
 */
abstract class RecyclerItem(
    override val id: Long,
    override val layout: Int,
    override val supportsSelectionMode: Boolean = false
) : IRecyclerItem {

    open fun createViewHolder(view: View) =
        SupportViewHolder(view)

    override val decorator =
        object : ISelectionDecorator { }
}
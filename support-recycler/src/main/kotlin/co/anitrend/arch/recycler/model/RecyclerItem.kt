package co.anitrend.arch.recycler.model

import android.view.View
import co.anitrend.arch.recycler.action.decorator.ISelectionDecorator
import co.anitrend.arch.recycler.common.ItemClickListener
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.recycler.model.contract.IRecyclerItemSpan

/**
 * A wrapper for items that can be displayed inside the recycler view
 *
 * @since v1.3.0
 *
 * @see IRecyclerItem
 * @see IRecyclerItemSpan
 */
abstract class RecyclerItem<H: SupportViewHolder>(
    override val id: Long,
    override val layout: Int,
    override val isClickable: Boolean = false,
    override val isLongClickable: Boolean = false,
    override val supportsActionMode: Boolean = false
) : IRecyclerItem, IRecyclerItemSpan {

    open fun createViewHolder(view: View) =
        SupportViewHolder(view)

    abstract fun bind(
        holder: H,
        position: Int,
        payloads: List<Any>
    )

    open fun bind(
        holder: H,
        position: Int,
        payloads: List<Any>,
        clickListener: ItemClickListener<RecyclerItem<*>>
    ) {
        holder.bind(this, clickListener)
    }

    abstract fun unbind(holder: H)

    override val decorator =
        object : ISelectionDecorator { }
}
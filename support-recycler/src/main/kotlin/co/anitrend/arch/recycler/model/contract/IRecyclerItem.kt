package co.anitrend.arch.recycler.model.contract

import android.view.View
import androidx.lifecycle.MutableLiveData
import co.anitrend.arch.recycler.action.decorator.ISelectionDecorator
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder

/**
 * Contract for recycler item
 *
 * @property id id of the item view
 * @property layout layout to inflate
 * @property isClickable whether or not the element supports top level click actions
 * @property isLongClickable whether or not the element supports top level long click actions
 * @property supportsSelectionMode whether or not the element can trigger action mode
 * @property decorator provides styling information
 *
 * @since v1.3.0
 */
interface IRecyclerItem {
    val id: Long

    val layout: Int

    val isClickable: Boolean
    val isLongClickable: Boolean
    val supportsSelectionMode: Boolean

    val decorator: ISelectionDecorator

    fun bind(
        view: View,
        position: Int,
        payloads: List<Any>,
        clickObservable: MutableLiveData<ClickableItem>
    )

    fun unbind(view: View)
}
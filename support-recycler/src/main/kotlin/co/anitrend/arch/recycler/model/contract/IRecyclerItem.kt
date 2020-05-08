package co.anitrend.arch.recycler.model.contract

import android.view.View
import androidx.lifecycle.MutableLiveData
import co.anitrend.arch.recycler.action.decorator.ISelectionDecorator
import co.anitrend.arch.recycler.common.ClickableItem

/**
 * Contract for recycler item
 *
 * @property id id of the item view
 * @property layout layout to inflate
 * @property decorator provides styling information
 * @property supportsSelectionMode whether or not the element can trigger action mode
 *
 * @see IRecyclerItemSpan
 *
 * @since v1.3.0
 */
interface IRecyclerItem : IRecyclerItemSpan {
    val id: Long

    val layout: Int

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
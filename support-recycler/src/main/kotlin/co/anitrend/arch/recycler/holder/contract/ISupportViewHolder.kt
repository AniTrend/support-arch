package co.anitrend.arch.recycler.holder.contract

import androidx.lifecycle.MutableLiveData
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.model.contract.IRecyclerItem

/**
 * General purpose recycler view holder to simplify construction of views
 *
 * @since 1.3.0
 */
interface ISupportViewHolder {

    /**
     * Load images, text, buttons, etc. in this method from the given parameter
     */
    fun bind(
        position: Int,
        payloads: List<Any>,
        model: IRecyclerItem,
        clickObservable: MutableLiveData<ClickableItem>,
        selectionMode: ISupportSelectionMode<Long>? = null
    )

    /**
     * Clear or unbind any references the views might be using, e.g. image loading
     * libraries, data binding, callbacks e.t.c
     */
    fun onViewRecycled()
}
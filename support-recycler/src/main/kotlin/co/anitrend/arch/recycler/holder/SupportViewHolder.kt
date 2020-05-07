package co.anitrend.arch.recycler.holder

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.model.contract.IRecyclerItem

open class SupportViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private var recyclerItem: IRecyclerItem? = null

    /**
     * Load images, text, buttons, etc. in this method from the given parameter
     */
    open fun bind(
        position: Int,
        payloads: List<Any>,
        model: IRecyclerItem?,
        clickObservable: MutableLiveData<ClickableItem>,
        selectionMode: ISupportSelectionMode<Long>?
    ) {
        recyclerItem = model
        model?.bind(itemView, position, payloads, clickObservable)
        if (model?.supportsSelectionMode == true && model.id != RecyclerView.NO_ID) {
            selectionMode?.apply {
                model.decorator.decorateUsing(
                    itemView,
                    containsItem(model.id)
                )
            }
        }
    }

    /**
     * Clear or unbind any references the views might be using, e.g. image loading
     * libraries, data binding, callbacks e.t.c
     */
    open fun onViewRecycled() {
        recyclerItem?.unbind(itemView)
        recyclerItem = null
    }
}
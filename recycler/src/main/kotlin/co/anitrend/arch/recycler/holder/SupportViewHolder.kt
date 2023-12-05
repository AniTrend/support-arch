/**
 * Copyright 2021 AniTrend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.anitrend.arch.recycler.holder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.contract.ISupportViewHolder
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * General purpose recycler view holder to simplify construction of views
 *
 * @see ISupportViewHolder
 * @since 1.3.0
 */
open class SupportViewHolder(
    binding: ViewBinding,
) : ISupportViewHolder, RecyclerView.ViewHolder(binding.root) {
    private var recyclerItem: IRecyclerItem? = null

    /**
     * Load images, text, buttons, etc. in this method from the given parameter
     */
    override fun bind(
        position: Int,
        payloads: List<Any>,
        model: IRecyclerItem,
        stateFlow: MutableStateFlow<ClickableItem>,
        selectionMode: ISupportSelectionMode<Long>?,
    ) {
        recyclerItem = model
        model.bind(itemView, position, payloads, stateFlow, selectionMode)
        if (model.supportsSelectionMode && model.id != RecyclerView.NO_ID) {
            val isSelected = selectionMode?.containsItem(model.id)
            model.decorator.decorateUsing(
                itemView,
                isSelected ?: false,
            )
        }
    }

    /**
     * Clear or unbind any references the views might be using, e.g. image loading
     * libraries, data binding, callbacks e.t.c
     */
    override fun onViewRecycled() {
        recyclerItem?.unbind(itemView)
        recyclerItem = null
    }
}

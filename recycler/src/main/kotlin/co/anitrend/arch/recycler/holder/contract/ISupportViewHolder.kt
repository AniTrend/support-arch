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

package co.anitrend.arch.recycler.holder.contract

import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import kotlinx.coroutines.flow.MutableStateFlow

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
        stateFlow: MutableStateFlow<ClickableItem>,
        selectionMode: ISupportSelectionMode<Long>? = null,
    )

    /**
     * Clear or unbind any references the views might be using, e.g. image loading
     * libraries, data binding, callbacks e.t.c
     */
    fun onViewRecycled()
}

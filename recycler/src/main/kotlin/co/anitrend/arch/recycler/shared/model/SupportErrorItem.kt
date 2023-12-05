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

package co.anitrend.arch.recycler.shared.model

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.ext.gone
import co.anitrend.arch.extension.ext.visible
import co.anitrend.arch.recycler.R
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.databinding.SupportLayoutStateErrorBinding
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.RecyclerItem
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * View holder for representing loading errors
 *
 * @since v1.2.0
 */
class SupportErrorItem(
    private val loadState: LoadState,
    private val configuration: IStateLayoutConfig,
) : RecyclerItem(RecyclerView.NO_ID) {
    private var binding: SupportLayoutStateErrorBinding? = null

    override fun bind(
        view: View,
        position: Int,
        payloads: List<Any>,
        stateFlow: MutableStateFlow<ClickableItem>,
        selectionMode: ISupportSelectionMode<Long>?,
    ) {
        binding = SupportLayoutStateErrorBinding.bind(view)
        if (loadState is LoadState.Error) {
            binding?.stateErrorText?.text = loadState.details.message
        }

        if (configuration.retryAction != null) {
            binding?.stateErrorAction?.visible()
            binding?.stateErrorAction?.setOnClickListener {
                stateFlow.value = ClickableItem.State(loadState, it)
            }
            binding?.stateErrorAction?.setText(configuration.retryAction!!)
        } else {
            binding?.stateErrorAction?.gone()
        }
    }

    override fun unbind(view: View) {
        binding?.stateErrorAction?.setOnClickListener(null)
        binding = null
    }

    override fun getSpanSize(
        spanCount: Int,
        position: Int,
        resources: Resources,
    ) = resources.getInteger(co.anitrend.arch.theme.R.integer.single_list_size)

    companion object {
        /**
         * Inflates a layout and returns it's root view wrapped in [SupportViewHolder]
         *
         * @param viewGroup parent view requesting the layout
         * @param layoutInflater inflater to use, this is derived from the [viewGroup]
         */
        internal fun createViewHolder(
            viewGroup: ViewGroup,
            layoutInflater: LayoutInflater,
        ): SupportViewHolder {
            val binding =
                SupportLayoutStateErrorBinding.inflate(
                    layoutInflater,
                    viewGroup,
                    false,
                )
            return SupportViewHolder(binding)
        }
    }
}

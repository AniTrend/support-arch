package co.anitrend.arch.recycler.shared.model

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.extension.ext.gone
import co.anitrend.arch.extension.ext.visible
import co.anitrend.arch.recycler.R
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.databinding.SupportLayoutStateLoadingBinding
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.RecyclerItem
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * View holder for representing loading status
 *
 * @since v0.9.X
 */
open class SupportLoadingItem(
    private val configuration: IStateLayoutConfig
) : RecyclerItem(RecyclerView.NO_ID) {

    private var binding: SupportLayoutStateLoadingBinding? = null

    override fun bind(
        view: View,
        position: Int,
        payloads: List<Any>,
        stateFlow: MutableStateFlow<ClickableItem>,
        selectionMode: ISupportSelectionMode<Long>?
    ) {
        binding = SupportLayoutStateLoadingBinding.bind(view)
        val message = configuration.loadingMessage
        if (message != null) {
            binding?.stateLoadingText?.visible()
            binding?.stateLoadingText?.setText(message)
        }
        else
            binding?.stateLoadingText?.gone()
    }

    override fun unbind(view: View) {
        binding = null
    }

    override fun getSpanSize(
        spanCount: Int,
        position: Int,
        resources: Resources
    ) = resources.getInteger(R.integer.single_list_size)

    companion object {
        /**
         * Inflates a layout and returns it's root view wrapped in [SupportViewHolder]
         *
         * @param viewGroup parent view requesting the layout
         * @param layoutInflater inflater to use, this is derived from the [viewGroup]
         */
        internal fun createViewHolder(
            viewGroup: ViewGroup,
            layoutInflater: LayoutInflater
        ): SupportViewHolder {
            val binding = SupportLayoutStateLoadingBinding.inflate(
                layoutInflater, viewGroup, false
            )
            return SupportViewHolder(binding)
        }
    }
}
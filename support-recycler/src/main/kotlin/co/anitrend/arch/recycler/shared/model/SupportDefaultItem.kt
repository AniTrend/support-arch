package co.anitrend.arch.recycler.shared.model

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.ext.gone
import co.anitrend.arch.extension.ext.visible
import co.anitrend.arch.recycler.R
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.RecyclerItem
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * View holder for representing idle or success states
 *
 * @since v1.3.0
 */
class SupportDefaultItem(
    private val loadState: LoadState,
    private val stateConfig: IStateLayoutConfig
) : RecyclerItem(RecyclerView.NO_ID) {

    override fun bind(
        view: View,
        position: Int,
        payloads: List<Any>,
        stateFlow: MutableStateFlow<ClickableItem>,
        selectionMode: ISupportSelectionMode<Long>?
    ) {
        val defaultMessage = stateConfig.defaultMessage
        if (loadState is LoadState.Success && defaultMessage != null) {
            view.visible()
            view.findViewById<AppCompatTextView>(
                R.id.stateDefaultText
            ).setText(defaultMessage)
        }
        else
            view.gone()
    }

    override fun unbind(view: View) {

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
            val view = layoutInflater.inflate(
                R.layout.support_layout_state_default,
                viewGroup,
                false
            )
            return SupportViewHolder(view)
        }
    }
}
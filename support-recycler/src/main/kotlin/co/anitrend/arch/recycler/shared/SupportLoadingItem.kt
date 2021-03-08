package co.anitrend.arch.recycler.shared

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.ext.gone
import co.anitrend.arch.extension.ext.visible
import co.anitrend.arch.recycler.R
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.RecyclerItem
import kotlinx.android.synthetic.main.support_layout_state_footer_loading.view.*
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * View holder for representing loading status
 *
 * @since v0.9.X
 */
open class SupportLoadingItem(
    private val configuration: IStateLayoutConfig
) : RecyclerItem(RecyclerView.NO_ID) {

    override fun bind(
        view: View,
        position: Int,
        payloads: List<Any>,
        stateFlow: MutableStateFlow<ClickableItem?>,
        selectionMode: ISupportSelectionMode<Long>?
    ) {
        if (configuration.loadingMessage != null) {
            view.stateFooterLoadingText.visible()
            view.stateFooterLoadingText.setText(configuration.loadingMessage!!)
        }
        else
            view.stateFooterLoadingText.gone()
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
                R.layout.support_layout_state_footer_loading,
                viewGroup,
                false
            )
            return SupportViewHolder(view)
        }
    }
}
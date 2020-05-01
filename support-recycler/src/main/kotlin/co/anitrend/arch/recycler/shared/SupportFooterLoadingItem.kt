package co.anitrend.arch.recycler.shared

import android.content.res.Resources
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.extension.gone
import co.anitrend.arch.recycler.R
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.RecyclerItem
import kotlinx.android.synthetic.main.support_layout_state_footer_loading.view.*

/**
 * Footer view holder for representing loading status
 *
 * @since v0.9.X
 */
open class SupportFooterLoadingItem(
    @LayoutRes layout: Int,
    private val configuration: IStateLayoutConfig
) : RecyclerItem<SupportViewHolder>(RecyclerView.NO_ID, layout) {

    override fun bind(holder: SupportViewHolder, position: Int, payloads: List<Any>) {
        if (configuration.loadingMessage != null)
            holder.itemView.stateFooterLoadingText.setText(configuration.loadingMessage!!)
        else
            holder.itemView.stateFooterLoadingText.gone()
    }

    override fun unbind(holder: SupportViewHolder) {

    }

    override fun getSpanSize(
        spanCount: Int,
        position: Int,
        resources: Resources
    ) = resources.getInteger(R.integer.single_list_size)
}
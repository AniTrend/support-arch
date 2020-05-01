package co.anitrend.arch.recycler.shared

import android.content.res.Resources
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.gone
import co.anitrend.arch.recycler.R
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.RecyclerItem
import kotlinx.android.synthetic.main.support_layout_state_footer_error.view.*

/**
 * Footer view holder for representing loading errors
 *
 * @since v1.2.0
 */
class SupportFooterErrorItem(
    @LayoutRes layout: Int,
    retryAction: View.OnClickListener?,
    private val networkState: NetworkState?,
    private val configuration: IStateLayoutConfig
) : RecyclerItem<SupportViewHolder>(RecyclerView.NO_ID, layout) {

    private val clickListener = View.OnClickListener {
        retryAction?.onClick(it)
    }

    override fun bind(holder: SupportViewHolder, position: Int, payloads: List<Any>) {
        if (networkState is NetworkState.Error)
            holder.itemView.stateFooterErrorText.text = networkState.message

        if (configuration.retryAction != null) {
            holder.itemView.stateFooterErrorAction.setOnClickListener(clickListener)
            holder.itemView.stateFooterErrorAction.setText(configuration.retryAction!!)
        }
        else
            holder.itemView.stateFooterErrorAction.gone()
    }

    override fun unbind(holder: SupportViewHolder) {
        holder.itemView.stateFooterErrorAction.setOnClickListener(null)
    }

    override fun getSpanSize(
        spanCount: Int,
        position: Int,
        resources: Resources
    ) = resources.getInteger(R.integer.single_list_size)
}
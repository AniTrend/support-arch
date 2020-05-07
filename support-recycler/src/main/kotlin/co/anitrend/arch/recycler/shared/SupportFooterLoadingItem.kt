package co.anitrend.arch.recycler.shared

import android.content.res.Resources
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.extension.gone
import co.anitrend.arch.recycler.R
import co.anitrend.arch.recycler.common.ClickableItem
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
    private val configuration: IStateLayoutConfig,
    private val resources: Resources
) : RecyclerItem(RecyclerView.NO_ID, layout) {

    override fun bind(
        view: View,
        position: Int,
        payloads: List<Any>,
        clickObservable: MutableLiveData<ClickableItem>
    ) {
        if (configuration.loadingMessage != null)
            view.stateFooterLoadingText.setText(configuration.loadingMessage!!)
        else
            view.stateFooterLoadingText.gone()
    }

    override fun unbind(view: View) {
        view.stateFooterLoadingText.text = null
    }

    override fun getSpanSize(
        spanCount: Int,
        position: Int
    ) = resources.getInteger(R.integer.single_list_size)
}
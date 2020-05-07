package co.anitrend.arch.recycler.shared

import android.content.res.Resources
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.gone
import co.anitrend.arch.recycler.R
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.common.FooterClickableItem
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
    private val resources: Resources,
    private val networkState: NetworkState?,
    private val configuration: IStateLayoutConfig
) : RecyclerItem(RecyclerView.NO_ID, layout) {

    override fun bind(
        view: View,
        position: Int,
        payloads: List<Any>,
        clickObservable: MutableLiveData<ClickableItem>
    ) {
        if (networkState is NetworkState.Error)
            view.stateFooterErrorText.text = networkState.message

        if (configuration.retryAction != null) {
            view.stateFooterErrorAction.setOnClickListener {
                clickObservable.postValue(
                    FooterClickableItem(it)
                )
            }
            view.stateFooterErrorAction.setText(configuration.retryAction!!)
        }
        else
            view.stateFooterErrorAction.gone()
    }

    override fun unbind(view: View) {
        view.stateFooterErrorAction.setOnClickListener(null)
    }

    override fun getSpanSize(
        spanCount: Int,
        position: Int
    ) = resources.getInteger(R.integer.single_list_size)
}
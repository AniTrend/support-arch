package co.anitrend.arch.ui.recycler.common

import android.view.View
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.gone
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder
import co.anitrend.arch.ui.recycler.holder.event.ItemClickListener
import co.anitrend.arch.ui.util.StateLayoutConfig
import kotlinx.android.synthetic.main.support_layout_state_footer_error.view.*

/**
 * Footer view holder for representing loading errors
 *
 * @since v1.2.0
 */
class SupportFooterErrorViewHolder<T>(
    view: View,
    retryAction: View.OnClickListener?,
    private val networkState: NetworkState?,
    private val config: StateLayoutConfig
) : SupportViewHolder<T>(view.rootView) {

    private val clickListener = View.OnClickListener {
        retryAction?.onClick(it)
    }

    /**
     * Load images, text, buttons, etc. in this method from the given parameter
     *
     * @param model Is the liveData at the current adapter position
     */
    override fun invoke(model: T?) {
        if (networkState is NetworkState.Error)
            itemView.stateFooterErrorText.text = networkState.message

        if (config.retryAction != null) {
            itemView.stateFooterErrorAction.setOnClickListener(clickListener)
            itemView.stateFooterErrorAction.setText(config.retryAction)
        }
        else
            itemView.stateFooterErrorAction.gone()
    }

    /**
     * Clear or unbind any references the views might be using, e.g. image loading
     * libraries, data binding, callbacks e.t.c
     */
    override fun onViewRecycled() {
        itemView.stateFooterErrorAction.setOnClickListener(null)
    }

    /**
     * Handle any onclick events from our views, optionally you can call
     * [performClick] to dispatch [Pair]<[Int], T> on the [ItemClickListener]
     *
     * @param view the view that has been clicked
     */
    override fun onItemClick(view: View, itemClickListener: ItemClickListener<T>) {}
}
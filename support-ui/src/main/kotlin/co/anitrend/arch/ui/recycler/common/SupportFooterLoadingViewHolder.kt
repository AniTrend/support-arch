package co.anitrend.arch.ui.recycler.common

import android.view.View
import co.anitrend.arch.extension.gone
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder
import co.anitrend.arch.ui.recycler.holder.event.ItemClickListener
import co.anitrend.arch.ui.util.SupportStateLayoutConfiguration
import kotlinx.android.synthetic.main.support_layout_state_footer_loading.view.*

/**
 * Footer view holder for representing loading status
 *
 * @since v0.9.X
 */
class SupportFooterLoadingViewHolder<T>(
    view: View,
    private val config: StateLayoutConfig
) : SupportViewHolder<T>(view.rootView) {

    /**
     * Load images, text, buttons, etc. in this method from the given parameter
     *
     * @param model Is the liveData at the current adapter position
     */
    override fun invoke(model: T?) {
        if (configuration.loadingMessage != null)
            view.stateFooterLoadingText.setText(configuration.loadingMessage)
        else
            view.stateFooterLoadingText.gone()
    }

    /**
     * Clear or unbind any references the views might be using, e.g. image loading
     * libraries, data binding, callbacks e.t.c
     */
    override fun onViewRecycled() {

    }

    /**
     * Handle any onclick events from our views, optionally you can call
     * [performClick] to dispatch [Pair]<[Int], T> on the [ItemClickListener]
     *
     * @param view the view that has been clicked
     */
    override fun onItemClick(view: View, itemClickListener: ItemClickListener<T>) { }
}
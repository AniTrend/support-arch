package co.anitrend.arch.ui.recycler.common

import android.view.View
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder
import co.anitrend.arch.ui.recycler.holder.event.ItemClickListener
import co.anitrend.arch.ui.util.SupportStateLayoutConfiguration
import co.anitrend.arch.ui.view.text.SingleLineTextView
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.support_layout_state_footer_error.view.*

/**
 * Footer view holder for representing loading errors
 *
 * @since v1.2.0
 */
class SupportFooterErrorViewHolder<T>(
    view: View,
    private val networkState: NetworkState?,
    private val retryAction: View.OnClickListener,
    private val configuration: SupportStateLayoutConfiguration
) : SupportViewHolder<T>(view.rootView) {

    /**
     * Load images, text, buttons, etc. in this method from the given parameter
     *
     * @param model Is the liveData at the current adapter position
     */
    override fun invoke(model: T?) {
        if (networkState is NetworkState.Error)
            view.stateFooterErrorText.text = networkState.message
        view.stateFooterErrorAction.setText(configuration.retryAction)
        view.stateFooterErrorAction.setOnClickListener(retryAction)
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
    override fun onItemClick(view: View, itemClickListener: ItemClickListener<T>) {}
}
package co.anitrend.arch.ui.recycler.common

import android.view.View
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder
import co.anitrend.arch.ui.recycler.holder.event.ItemClickListener
import co.anitrend.arch.ui.util.SupportStateLayoutConfiguration
import co.anitrend.arch.ui.view.text.SingleLineTextView

/**
 * Footer view holder for representing loading status
 *
 * @since v0.9.X
 */
class SupportFooterLoadingViewHolder<T>(
    view: View,
    private val configuration: SupportStateLayoutConfiguration
) : SupportViewHolder<T>(view.rootView) {

    private val stateText : SingleLineTextView? = view.findViewById(R.id.stateFooterLoadingText)

    /**
     * Load images, text, buttons, etc. in this method from the given parameter
     *
     * @param model Is the liveData at the current adapter position
     */
    override fun invoke(model: T?) {
        stateText?.setText(configuration.loadingMessage)
    }

    /**
     * If any image views are used within the view holder, clear any pending async requests
     * by using [com.bumptech.glide.RequestManager.clear]
     *
     * @see com.bumptech.glide.Glide
     */
    override fun onViewRecycled() { }

    /**
     * Handle any onclick events from our views, optionally you can call
     * [performClick] to dispatch [Pair]<[Int], T> on the [ItemClickListener]
     *
     * @param view the view that has been clicked
     */
    override fun onItemClick(view: View, itemClickListener: ItemClickListener<T>) { }
}
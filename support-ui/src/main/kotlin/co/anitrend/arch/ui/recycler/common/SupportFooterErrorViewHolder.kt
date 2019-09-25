package co.anitrend.arch.ui.recycler.common

import android.view.View
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.ui.R
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder
import co.anitrend.arch.ui.util.SupportStateLayoutConfiguration
import co.anitrend.arch.ui.view.text.SingleLineTextView
import com.google.android.material.button.MaterialButton

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

    private val stateText = view.findViewById<SingleLineTextView>(R.id.stateFooterErrorText)
    private val stateAction = view.findViewById<MaterialButton>(R.id.stateFooterErrorAction)

    /**
     * Load images, text, buttons, etc. in this method from the given parameter
     *
     * @param model Is the liveData at the current adapter position
     */
    override fun invoke(model: T?) {
        if (networkState is NetworkState.Error)
            stateText.text = networkState.message
        stateAction.setText(configuration.retryAction)
        stateAction.setOnClickListener(retryAction)
    }

    /**
     * If any image views are used within the view holder, clear any pending async requests
     * by using [com.bumptech.glide.RequestManager.clear]
     *
     * @see com.bumptech.glide.Glide
     */
    override fun onViewRecycled() {}

    /**
     * Handle any onclick events from our views, optionally you can call
     * [performClick] to dispatch [Pair]<[Int], T> on the [ItemClickListener]
     *
     * @param view the view that has been clicked
     */
    override fun onItemClick(view: View) {}
}
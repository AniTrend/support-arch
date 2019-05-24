package io.wax911.support.ui.recycler.adapter

import android.view.View
import android.widget.ProgressBar
import io.wax911.support.ui.R
import io.wax911.support.ui.recycler.holder.SupportViewHolder
import io.wax911.support.ui.view.text.SingleLineTextView

class SupportFooterViewHolder<T>(view: View) : SupportViewHolder<T>(view.rootView) {

    private val stateText : SingleLineTextView? = view.findViewById(R.id.stateText)
    private val stateProgress : ProgressBar? = view.findViewById(R.id.stateProgress)

    /**
     * Load image, text, buttons, etc. in this method from the given parameter
     *
     * @param model Is the liveData at the current adapter position
     */
    override fun onBindViewHolder(model: T?) {
        stateText?.setText(R.string.supportTextLoading)
    }

    /**
     * If any image views are used within the view holder, clear any pending async img requests
     * by using Glide.clear(ImageView) or Glide.with(context).clear(view) if using Glide v4.0
     */
    override fun onViewRecycled() {

    }

    /**
     * Handle any onclick events from our views
     *
     * @param v the view that has been clicked
     * @see View.OnClickListener
     */
    override fun onClick(v: View) {

    }
}
package io.wax911.sample.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.wax911.sample.data.model.show.Show
import io.wax911.sample.databinding.AdapterMediaItemBinding
import io.wax911.support.core.presenter.SupportPresenter
import io.wax911.support.ui.recycler.adapter.SupportViewAdapter
import io.wax911.support.ui.recycler.holder.SupportViewHolder
import io.wax911.support.ui.recycler.holder.event.ItemClickListener

class ShowAdapter(
    presenter: SupportPresenter<*>,
    itemClickListener: ItemClickListener<Show>
) : SupportViewAdapter<Show>(presenter, itemClickListener) {
    /**
     * Should provide the required view holder, this function is a substitute for [onCreateViewHolder] which now
     * has extended functionality
     */
    override fun createDefaultViewHolder(parent: ViewGroup, viewType: Int, layoutInflater: LayoutInflater): SupportViewHolder<Show> {
        return ShowViewHolder(AdapterMediaItemBinding.inflate(layoutInflater, parent, false))
    }


    inner class ShowViewHolder(
        private val binding: AdapterMediaItemBinding
    ): SupportViewHolder<Show>(binding.root) {

        /**
         * Load image, text, buttons, etc. in this method from the given parameter
         *
         * @param model Is the liveData at the current adapter position
         */
        override fun onBindViewHolder(model: Show?) {
            with (binding) {
                entity = model
                executePendingBindings()
            }
        }

        /**
         * If any image views are used within the view holder, clear any pending async img requests
         * by using Glide.clear(ImageView) or Glide.with(context).clear(view) if using Glide v4.0
         */
        override fun onViewRecycled() {
            with(binding) {
                showTrailer.onViewRecycled()
                unbind()
            }
        }

        /**
         * Handle any onclick events from our views
         * <br></br>
         *
         * @param v the view that has been clicked
         * @see View.OnClickListener
         */
        override fun onClick(v: View) {
            performClick(binding.entity, v)
        }
    }
}
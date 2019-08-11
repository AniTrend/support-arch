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
    private val clickListener: ItemClickListener<Show>
) : SupportViewAdapter<Show>(presenter) {

    /**
     * Should provide the required view holder, this function is a substitute for [onCreateViewHolder] which now
     * has extended functionality
     */
    override fun createDefaultViewHolder(
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater
    ): SupportViewHolder<Show> {
        return ShowViewHolder(AdapterMediaItemBinding.inflate(layoutInflater, parent, false))
    }


    inner class ShowViewHolder(
        private val binding: AdapterMediaItemBinding
    ): SupportViewHolder<Show>(binding.root) {

        /**
         * Load images, text, buttons, etc. in this method from the given parameter
         *
         * @param model Is the liveData at the current adapter position
         */
        override fun invoke(model: Show?) {
            with (binding) {
                entity = model
                executePendingBindings()
            }
        }

        /**
         * If any image views are used within the view holder, clear any pending async requests
         * by using [com.bumptech.glide.RequestManager.clear]
         *
         * @see com.bumptech.glide.Glide
         */
        override fun onViewRecycled() {
            with(binding) {
                showTrailer.onViewRecycled()
                unbind()
            }
        }

        /**
         * Handle any onclick events from our views, optionally you can call
         * [performClick] to dispatch [Pair]<[Int], T> on the [ItemClickListener]
         *
         * @param view the view that has been clicked
         */
        override fun onItemClick(view: View) {
            performClick(
                clickListener = clickListener,
                entity = binding.entity as Show?,
                view = view
            )
        }
    }
}
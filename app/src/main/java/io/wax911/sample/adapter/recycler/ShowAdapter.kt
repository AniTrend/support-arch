package io.wax911.sample.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.wax911.sample.data.entitiy.show.ShowEntity
import io.wax911.sample.databinding.AdapterMediaItemBinding
import co.anitrend.arch.core.presenter.SupportPresenter
import co.anitrend.arch.ui.recycler.adapter.SupportViewAdapter
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder
import co.anitrend.arch.ui.recycler.holder.event.ItemClickListener

class ShowAdapter(
    presenter: SupportPresenter<*>,
    private val clickListener: ItemClickListener<ShowEntity>
) : SupportViewAdapter<ShowEntity>(presenter) {

    /**
     * Should provide the required view holder, this function is a substitute for [onCreateViewHolder] which now
     * has extended functionality
     */
    override fun createDefaultViewHolder(
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater
    ): SupportViewHolder<ShowEntity> {
        return ShowViewHolder(AdapterMediaItemBinding.inflate(layoutInflater, parent, false))
    }


    inner class ShowViewHolder(
        private val binding: AdapterMediaItemBinding
    ): SupportViewHolder<ShowEntity>(binding.root) {

        /**
         * Load images, text, buttons, etc. in this method from the given parameter
         *
         * @param model Is the liveData at the current adapter position
         */
        override fun invoke(model: ShowEntity?) {
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
                entity = binding.entity as ShowEntity?,
                view = view
            )
        }
    }
}
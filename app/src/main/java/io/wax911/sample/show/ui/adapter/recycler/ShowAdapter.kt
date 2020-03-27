package io.wax911.sample.show.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.core.presenter.SupportPresenter
import co.anitrend.arch.ui.recycler.adapter.SupportPagedListAdapter
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder
import co.anitrend.arch.ui.recycler.holder.event.ItemClickListener
import co.anitrend.arch.ui.util.SupportStateLayoutConfiguration
import io.wax911.sample.data.entitiy.show.ShowEntity
import io.wax911.sample.databinding.AdapterMediaItemBinding

class ShowAdapter(
    private val clickListener: ItemClickListener<ShowEntity>,
    override val stateConfiguration: SupportStateLayoutConfiguration
) : SupportPagedListAdapter<ShowEntity>() {

    /**
     * Used to get stable ids for [androidx.recyclerview.widget.RecyclerView.Adapter] but only if
     * [androidx.recyclerview.widget.RecyclerView.Adapter.setHasStableIds] is set to true.
     *
     * The identifiable id of each item should unique, and if non exists
     * then this function should return [androidx.recyclerview.widget.RecyclerView.NO_ID]
     */
    override fun getStableIdFor(item: ShowEntity?): Long {
        return item?.id?.toLong() ?: RecyclerView.NO_ID
    }

    /**
     * Should provide the required view holder, this function is a substitute for [onCreateViewHolder] which now
     * has extended functionality
     */
    override fun createDefaultViewHolder(
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater
    ): SupportViewHolder<ShowEntity> {
        val viewHolder = ShowViewHolder(
            AdapterMediaItemBinding.inflate(layoutInflater, parent, false)
        )

        viewHolder.itemView.setOnClickListener {
            viewHolder.onItemClick(it, clickListener)
        }

        return viewHolder
    }


    class ShowViewHolder(
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

        override fun onItemClick(view: View, itemClickListener: ItemClickListener<ShowEntity>) {
            performClick(
                clickListener = itemClickListener,
                entity = binding.entity as ShowEntity?,
                view = view
            )
        }
    }
}
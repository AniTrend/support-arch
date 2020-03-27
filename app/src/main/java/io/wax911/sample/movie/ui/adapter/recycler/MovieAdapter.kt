package io.wax911.sample.movie.ui.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.core.presenter.SupportPresenter
import co.anitrend.arch.ui.recycler.adapter.SupportPagedListAdapter
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder
import co.anitrend.arch.ui.recycler.holder.event.ItemClickListener
import co.anitrend.arch.ui.util.SupportStateLayoutConfiguration
import io.wax911.sample.data.entitiy.movie.MovieEntity
import io.wax911.sample.databinding.AdapterMediaItemBinding

class MovieAdapter(
    private val clickListener: ItemClickListener<MovieEntity>,
    override val stateConfiguration: SupportStateLayoutConfiguration
) : SupportPagedListAdapter<MovieEntity>() {

    /**
     * Used to get stable ids for [androidx.recyclerview.widget.RecyclerView.Adapter] but only if
     * [androidx.recyclerview.widget.RecyclerView.Adapter.setHasStableIds] is set to true.
     *
     * The identifiable id of each item should unique, and if non exists
     * then this function should return [androidx.recyclerview.widget.RecyclerView.NO_ID]
     */
    override fun getStableIdFor(item: MovieEntity?): Long {
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
    ): SupportViewHolder<MovieEntity> {
        val viewHolder = MovieViewHolder(
            AdapterMediaItemBinding.inflate(layoutInflater, parent, false)
        )

        viewHolder.itemView.setOnClickListener {
            viewHolder.onItemClick(it, clickListener)
        }

        return viewHolder
    }

    class MovieViewHolder(
        private val binding: AdapterMediaItemBinding
    ) : SupportViewHolder<MovieEntity>(binding.root) {

        /**
         * Load images, text, buttons, etc. in this method from the given parameter
         *
         * @param model Is the liveData at the current adapter position
         */
        override fun invoke(model: MovieEntity?) {
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

        override fun onItemClick(view: View, itemClickListener: ItemClickListener<MovieEntity>) {
            performClick(
                clickListener = itemClickListener,
                entity = binding.entity as MovieEntity?,
                view = view
            )
        }
    }
}
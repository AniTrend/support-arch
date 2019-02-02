package io.wax911.sample.adapter.recycler

import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import io.wax911.sample.R
import io.wax911.sample.model.BaseModel
import io.wax911.support.recycler.adapter.SupportViewAdapter
import io.wax911.support.recycler.holder.SupportViewHolder
import io.wax911.support.getLayoutInflater

class SimpleModelAdapter: SupportViewAdapter<BaseModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportViewHolder<BaseModel> {
        val inflatedView = parent.context.getLayoutInflater()
                .inflate(R.layout.support_layout_state_footer, parent, false)
        return SimpleModelViewHolder(inflatedView)
    }

    private class SimpleModelViewHolder(view : View) : SupportViewHolder<BaseModel>(view) {

        /**
         * Load image, text, buttons, etc. in this method from the given parameter
         * <br></br>
         *
         * @param model Is the liveData at the current adapter position
         */
        override fun onBindViewHolder(model: BaseModel) {

        }

        /**
         * If any image views are used within the view holder, clear any pending async img requests
         * by using Glide.clear(ImageView) or Glide.with(context).clear(view) if using Glide v4.0
         */
        override fun onViewRecycled() {

        }

        /**
         * Handle any onclick events from our views
         * <br></br>
         *
         * @param v the view that has been clicked
         * @see View.OnClickListener
         */
        override fun onClick(v: View) {

        }
    }
}
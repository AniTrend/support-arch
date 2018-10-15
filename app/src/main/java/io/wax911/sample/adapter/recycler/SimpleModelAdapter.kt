package io.wax911.sample.adapter.recycler

import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import io.wax911.sample.R
import io.wax911.sample.model.BaseModel
import io.wax911.support.custom.recycler.SupportViewAdapter
import io.wax911.support.custom.recycler.SupportViewHolder
import io.wax911.support.getLayoutInflater
import io.wax911.support.util.InstanceUtilNoArg

class SimpleModelAdapter private constructor() : SupportViewAdapter<BaseModel>() {

    companion object : InstanceUtilNoArg<SupportViewAdapter<BaseModel>>({ SimpleModelAdapter() })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportViewHolder<BaseModel> {
        val inflatedView = parent.context.getLayoutInflater()
                .inflate(R.layout.support_layout_state_footer, parent, false)
        return SimpleModelViewHolder(inflatedView)
    }

    /**
     *
     * Returns a filter that can be used to constrain data with a filtering
     * pattern.
     *
     *
     * This method is usually implemented by [android.widget.Adapter]
     * classes.
     *
     * @return a filter used to constrain data
     */
    override fun getFilter(): Filter {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private class SimpleModelViewHolder(view : View) : SupportViewHolder<BaseModel>(view) {

        /**
         * Load image, text, buttons, etc. in this method from the given parameter
         * <br></br>
         *
         * @param model Is the mutableLiveData at the current adapter position
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


        /**
         * Called when a view has been clicked and held.
         *
         * @param v The view that was clicked and held.
         *
         * @return true if the callback consumed the long click, false otherwise.
         */
        override fun onLongClick(v: View): Boolean {
            return false
        }

    }
}
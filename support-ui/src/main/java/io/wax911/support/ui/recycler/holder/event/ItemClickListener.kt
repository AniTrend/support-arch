package io.wax911.support.ui.recycler.holder.event

import android.view.View

/**
 * Created by max on 2017/11/15.
 * a click listener for view holders
 */

interface ItemClickListener<T> {

    /**
     * When the target view from [View.OnClickListener]
     * is clicked from a view holder this method will be called
     *
     * @param target view that has been clicked
     * @param data the liveData that at the click index
     */
    fun onItemClick(target: View, data: Pair<Int ,T?>)

    /**
     * When the target view from [View.OnLongClickListener]
     * is clicked from a view holder this method will be called
     *
     * @param target view that has been long clicked
     * @param data the liveData that at the long click index
     */
    fun onItemLongClick(target: View, data: Pair<Int ,T?>)
}

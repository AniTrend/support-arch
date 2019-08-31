package co.anitrend.arch.ui.recycler.holder.event

import android.view.View

/**
 * A click listener for view adapters with additional parameters for position
 * and model that has been interacted with
 *
 * @since 1.1.0
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

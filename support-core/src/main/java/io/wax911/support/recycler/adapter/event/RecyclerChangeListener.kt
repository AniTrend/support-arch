package io.wax911.support.recycler.adapter.event

/**
 * Created by max on 2017/12/04.
 * data change listeners
 */

interface RecyclerChangeListener<T> {

    fun onItemsInserted(swap: List<T>)

    fun onItemRangeInserted(swap: List<T>)

    fun onItemRangeChanged(swap: List<T>)

    fun onItemChanged(swap: T, position: Int)

    fun onItemRemoved(position: Int)
}

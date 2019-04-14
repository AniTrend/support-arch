package io.wax911.support.core.recycler.event

/**
 * Created by max on 2017/06/09.
 * Interface used to trigger loading of more results, when paginating
 */

interface RecyclerLoadListener {
    fun onLoadMore()
}

package co.anitrend.arch.recycler.extensions

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter

/**
 * Check if adapter is empty
 */
fun ISupportAdapter<*>.isEmpty(): Boolean {
    val count = when (this) {
        is ListAdapter<*, *> -> itemCount
        is PagedListAdapter<*, *> -> itemCount
        is RecyclerView.Adapter<*> -> itemCount
        else -> throw IllegalStateException(
            "Not sure how to request item count from: $this"
        )
    }
    return count < 1
}
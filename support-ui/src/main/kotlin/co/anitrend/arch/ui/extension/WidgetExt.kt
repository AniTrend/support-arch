package co.anitrend.arch.ui.extension

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.anitrend.arch.extension.getColorFromAttr
import co.anitrend.arch.recycler.SupportRecyclerView
import co.anitrend.arch.recycler.adapter.SupportListAdapter
import co.anitrend.arch.recycler.adapter.SupportPagedListAdapter
import co.anitrend.arch.ui.R

private fun setUpRecyclerConfiguration(
    vertical: Boolean,
    recyclerView: SupportRecyclerView,
    recyclerLayoutManager: RecyclerView.LayoutManager?
) {
    with (recyclerView) {
        setHasFixedSize(true)
        isNestedScrollingEnabled = true
        layoutManager = recyclerLayoutManager
            ?: when {
                vertical ->
                    StaggeredGridLayoutManager(
                        context.resources.getInteger(R.integer.grid_list_x3),
                        StaggeredGridLayoutManager.VERTICAL
                    )
                else ->
                    StaggeredGridLayoutManager(
                        context.resources.getInteger(R.integer.single_list_size),
                        StaggeredGridLayoutManager.HORIZONTAL
                    )
            }
    }
}

/**
 * Sets up a recycler view by handling all the boilerplate code associated with it using
 * the given layout manager or the default.
 *
 * @param supportAdapter recycler view adapter which will be used
 * @param vertical if the layout adapter should be vertical or horizontal
 * @param recyclerLayoutManager optional layout manager if you do not wish to use the default
 *
 * @since v1.3.X
 */
@Deprecated("Consider migrating to the new support-recycler module for more control")
fun SupportRecyclerView.setUpWith(
    vertical: Boolean = true,
    supportAdapter: RecyclerView.Adapter<*>,
    recyclerLayoutManager: RecyclerView.LayoutManager? = null
) {
    setUpRecyclerConfiguration(vertical, this, recyclerLayoutManager)
    adapter = supportAdapter
}

/**
 * This method applies the most common stateConfiguration for the widget,
 * things like direction, colors, behavior etc.
 */
fun SwipeRefreshLayout.configureWidgetBehaviorWith() {
    setProgressBackgroundColorSchemeColor(context.getColorFromAttr(R.attr.rootColor))
    setColorSchemeColors(context.getColorFromAttr(R.attr.contentColor))
}

/**
 * Resets the refreshing or loading states when called,
 * common use case would be after a network response
 */
fun SwipeRefreshLayout.onResponseResetStates() {
    if (isRefreshing) isRefreshing = false
}

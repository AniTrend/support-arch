package io.wax911.support.ui.extension

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nguyenhoanglam.progresslayout.ProgressLayout
import io.wax911.support.core.presenter.SupportPresenter
import io.wax911.support.core.recycler.adapter.SupportViewAdapter
import io.wax911.support.extension.getColorFromAttr
import io.wax911.support.extension.getCompatDrawable
import io.wax911.support.extension.getNavigationBarHeight
import io.wax911.support.extension.gone
import io.wax911.support.ui.R
import io.wax911.support.ui.recycler.SupportRecyclerView
import io.wax911.support.ui.view.widget.SupportRefreshLayout

/**
 * Sets up a recycler view by handling all the boilerplate code associated with it using
 * the given layout manager or the default.
 *
 * @param supportAdapter recycler view adapter which will be used
 * @param vertical if the layout adapter should be vertical or horizontal
 * @param recyclerLayoutManager optional layout manager if you do not wish to use the default
 */
fun SupportRecyclerView.setUpWith(supportAdapter: SupportViewAdapter<*>, vertical: Boolean = true,
                                  recyclerLayoutManager: RecyclerView.LayoutManager? = null) {
    setHasFixedSize(true)
    isNestedScrollingEnabled = true
    layoutManager = when (recyclerLayoutManager == null) {
        vertical ->
            StaggeredGridLayoutManager(
                context.resources.getInteger(R.integer.grid_list_x3),
                StaggeredGridLayoutManager.VERTICAL)
        !vertical ->
            StaggeredGridLayoutManager(
                context.resources.getInteger(R.integer.single_list_size),
                StaggeredGridLayoutManager.HORIZONTAL)
        else ->
            recyclerLayoutManager
    }
    adapter = supportAdapter
}

/**
 * This method applies the most common configuration for the widget, things like direction, colors, behavior etc.
 */
fun SupportRefreshLayout.configureWidgetBehaviorWith(context: FragmentActivity?, presenter : SupportPresenter<*>) = context?.also {
    setDragTriggerDistance(SupportRefreshLayout.DIRECTION_BOTTOM, (it.resources.getNavigationBarHeight()))
    setProgressBackgroundColorSchemeColor(it.getColorFromAttr(R.attr.rootColor))
    setColorSchemeColors(it.getColorFromAttr(R.attr.contentColor))
    setPermitRefresh(presenter.isPager)
    setPermitLoad(false)
    gone()
}

/**
 * Resets the refreshing or loading states when called, common use case would be after a network response
 */
fun SupportRefreshLayout.onResponseResetStates() {
    if (isRefreshing) isRefreshing = false
    if (isLoading) isLoading = false
}

fun Context?.showContentError(progressLayout: ProgressLayout, @StringRes message: Int, @StringRes retryButtonText : Int,
                              stateLayoutOnClick: View.OnClickListener) = this?.also {
    when {
        progressLayout.isLoading || progressLayout.isEmpty || progressLayout.isContent -> {
            progressLayout.showError(getCompatDrawable(R.drawable.ic_support_empty_state),
                it.getString(message), it.getString(retryButtonText), stateLayoutOnClick)
        }
        else -> {
        }
    }
}

fun ProgressLayout.showContentLoading() = when {
    isContent || isEmpty ||
            isError -> showLoading()
    else -> { }
}

fun ProgressLayout.showLoadedContent() = when {
    isLoading || isEmpty ||
            isError -> showContent()
    else -> { }
}

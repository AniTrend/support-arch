package io.wax911.support.recycler.event

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by max on 2017/06/09.
 * This class represents a custom OnScrollListener for RecyclerView which allow us to pre-fetch
 * data when user reaches the bottom in the list.
 *
 * Made By https://gist.github.com/Hochland/aca2f9152c1ff22d3b09f515530ac52b
 * Implementing original gist: https://gist.github.com/ssinss/e06f12ef66c51252563e
 * Modified by max to accommodate grid and staggered layout managers and other custom properties
 */
abstract class SupportScrollListener : RecyclerView.OnScrollListener() {

    var isPagingLimit = false
    var isPager = true

    private var mLoadListener: RecyclerLoadListener? = null
    private var mGridLayoutManager: GridLayoutManager? = null
    private var mStaggeredGridLayoutManager: StaggeredGridLayoutManager? = null

    private var mPreviousTotal = 0 // The total number of items in the dataset after the last load
    private var mLoading = true // True if still waiting for the last set of data to load.
    private val mVisibleThreshold = 3 //minimum allowed threshold before next page reload request

    /**
     * @return the current pagination page number
     */
    var currentPage = 1

    /**
     * @return the current pagination offset
     */
    var currentOffset = 0

    /**
     * @return true if this is the first page and also this is a paging fragment
     */
    fun isFirstPage() : Boolean =
            currentPage == 1 && isPager

    fun initListener(gridLayoutManager: GridLayoutManager, mLoadListener: RecyclerLoadListener) {
        mGridLayoutManager = gridLayoutManager
        this.mLoadListener = mLoadListener
    }

    fun initListener(staggeredGridLayoutManager: StaggeredGridLayoutManager, mLoadListener: RecyclerLoadListener) {
        mStaggeredGridLayoutManager = staggeredGridLayoutManager
        this.mLoadListener = mLoadListener
    }

    /**
     * Provides pagination size for calculating offsets
     */
    abstract fun paginationSize() : Int

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        var mTotalItemCount = 0
        var mFirstVisibleItem = 0
        val mVisibleItemCount = recyclerView.childCount

        //minimum allowed threshold before next page reload request
        mGridLayoutManager?.let {
            mTotalItemCount = it.itemCount
            mFirstVisibleItem = it.findFirstVisibleItemPosition()
        }
        mStaggeredGridLayoutManager?.let {
            mTotalItemCount = it.itemCount
            val firstPositions = it.findFirstVisibleItemPositions(null)
            if(firstPositions != null && firstPositions.isNotEmpty())
                mFirstVisibleItem = firstPositions[0]
        }

        //minimum allowed threshold before next page reload request
        when (mLoading) {
            true -> when (mTotalItemCount > mPreviousTotal ) {
                true -> {
                    mLoading = false
                    mPreviousTotal = mTotalItemCount
                }
            }
            false -> when (mTotalItemCount - mVisibleItemCount <= mFirstVisibleItem + mVisibleThreshold) {
                true -> {
                    currentPage++
                    currentOffset += paginationSize()
                    mLoadListener!!.onLoadMore()
                    mLoading = true
                }
            }
        }
    }

    /**
     * Should be used when refreshing a layout
     */
    fun onRefreshPage() {
        mLoading = true
        mPreviousTotal = 0
        currentPage = 1
        currentOffset = 0
    }
}

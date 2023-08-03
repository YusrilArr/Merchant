package com.gpay.merchantapp.utils

abstract class EndlessRecyclerOnScrollListener(private val mLinearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager) : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

    private var previousTotal = 0 // The total number of items in the dataset after the last load
    private var loading = true // True if we are still waiting for the last set of data to load.
    private val visibleThreshold = 5 // The minimum amount of items to have below your current scroll position before loading more.
    internal var firstVisibleItem: Int = 0
    internal var visibleItemCount: Int = 0
    internal var totalItemCount: Int = 0
    private var lastVisibleItem: Int = 0

    private var current_page = 1

    override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        totalItemCount = mLinearLayoutManager.itemCount
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()
        lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition()


        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
                return
            }
        }
        if (!loading && totalItemCount <= lastVisibleItem + visibleThreshold) {
            // End has been reached

            // Do something
            current_page++

            onLoadMore(current_page)

            loading = true
        }
    }

    abstract fun onLoadMore(current_page: Int)

    companion object {
        var TAG = EndlessRecyclerOnScrollListener::class.java.simpleName
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }
}

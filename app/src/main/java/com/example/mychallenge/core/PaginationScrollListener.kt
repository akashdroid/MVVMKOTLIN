package com.example.mychallenge.core

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class PaginationScrollListener(internal var layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    abstract val totalPageCount: Int

    abstract val isLastPage: Boolean

    abstract val isLoading: Boolean

   override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.getChildCount()
        val totalItemCount = layoutManager.getItemCount()
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading && !isLastPage) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
}
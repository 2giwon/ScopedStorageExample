package com.egiwon.scopedstorageexample.ext

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


@BindingAdapter("isRefreshing")
fun SwipeRefreshLayout.isRefreshing(isRefresh: Boolean) {
    isRefreshing = isRefresh
}
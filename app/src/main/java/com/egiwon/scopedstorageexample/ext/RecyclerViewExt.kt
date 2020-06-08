package com.egiwon.scopedstorageexample.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egiwon.scopedstorageexample.base.BaseAdapter

@Suppress("UNCHECKED_CAST")
@BindingAdapter("replaceItems")
fun RecyclerView.replaceItems(items: List<Any>?) {
    (adapter as? BaseAdapter<Any, *>)?.run {
        replaceItems(items)
        notifyDataSetChanged()
    }
}
package com.egiwon.scopedstorageexample.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egiwon.scopedstorageexample.base.BaseAdapter
import com.egiwon.scopedstorageexample.image.ImagePickerAdapter
import com.egiwon.scopedstorageexample.model.MediaStoreImage

@Suppress("UNCHECKED_CAST")
@BindingAdapter("replaceItems")
fun RecyclerView.replaceItems(items: List<Any>?) {
    (adapter as? BaseAdapter<Any, *>)?.run {
        replaceItems(items)
        notifyDataSetChanged()
    }
}

@BindingAdapter("replaceImages")
fun RecyclerView.replaceImages(items: List<MediaStoreImage>?) {
    (adapter as? ImagePickerAdapter)?.run {
        replaceItems(items)
    }
}
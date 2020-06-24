package com.egiwon.scopedstorageexample.ext

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.egiwon.scopedstorageexample.R
import com.egiwon.scopedstorageexample.util.GlideWrapper

@BindingAdapter("directorySrc")
fun AppCompatImageView.directorySrc(isDirectory: Boolean) {
    setImageDrawable(
        if (isDirectory) resources.getDrawable(R.drawable.ic_folder, null) else null
    )
}

@BindingAdapter("loadAsyncImage", "thumbnailSize")
fun ImageView.loadAsyncThumbnailImage(uri: String, size: Float) =
    GlideWrapper.loadThumbnailImage(this, uri, size)
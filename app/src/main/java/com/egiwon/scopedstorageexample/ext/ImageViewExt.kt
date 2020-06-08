package com.egiwon.scopedstorageexample.ext

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.egiwon.scopedstorageexample.R

@BindingAdapter("directorySrc")
fun AppCompatImageView.directorySrc(isDirectory: Boolean) {
    setImageDrawable(
        if (isDirectory) resources.getDrawable(R.drawable.ic_folder, null) else null
    )
}
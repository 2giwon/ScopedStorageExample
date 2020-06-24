package com.egiwon.scopedstorageexample.model

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil

data class MediaStoreImage(
    val id: Long,
    val isSelected: MutableLiveData<Boolean>,
    val contentUri: Uri
) {
    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<MediaStoreImage>() {
            override fun areItemsTheSame(oldItem: MediaStoreImage, newItem: MediaStoreImage) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MediaStoreImage, newItem: MediaStoreImage) =
                oldItem == newItem
        }
    }
}
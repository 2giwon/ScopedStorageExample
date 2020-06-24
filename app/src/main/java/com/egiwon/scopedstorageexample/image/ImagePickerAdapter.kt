package com.egiwon.scopedstorageexample.image

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egiwon.scopedstorageexample.R
import com.egiwon.scopedstorageexample.databinding.ItemImageBinding
import com.egiwon.scopedstorageexample.model.MediaStoreImage
import com.egiwon.scopedstorageexample.util.GlideWrapper

class ImagePickerAdapter(
    private val viewModel: ImagePickerViewModel
) : ListAdapter<MediaStoreImage, ImagePickerAdapter.ImageViewHolder>(MediaStoreImage.DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(parent = parent, viewModel = viewModel)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) =
        holder.onBind(getItem(position))

    override fun onViewRecycled(holder: ImageViewHolder) {
        super.onViewRecycled(holder)
        holder.recycledImage()
    }

    fun replaceItems(items: List<MediaStoreImage>?) {
        if (items != null) {
            submitList(items)
        }
    }

    inner class ImageViewHolder(
        parent: ViewGroup,
        @LayoutRes layoutResId: Int = R.layout.item_image,
        private val viewModel: ImagePickerViewModel
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(layoutResId, parent, false)
    ) {
        private val binding: ItemImageBinding = requireNotNull(DataBindingUtil.bind(itemView))

        fun onBind(item: Any?) {
            binding.mediaStoreImage = item as MediaStoreImage
        }

        fun recycledImage() = GlideWrapper.recycledImage(binding.ivImage)
    }
}
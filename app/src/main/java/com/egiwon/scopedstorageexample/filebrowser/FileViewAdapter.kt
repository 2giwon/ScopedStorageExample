package com.egiwon.scopedstorageexample.filebrowser

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.egiwon.scopedstorageexample.R
import com.egiwon.scopedstorageexample.base.BaseAdapter
import com.egiwon.scopedstorageexample.base.BaseViewHolder
import com.egiwon.scopedstorageexample.databinding.ItemFileBinding
import com.egiwon.scopedstorageexample.model.DocumentItem

class FileViewAdapter(
    @LayoutRes private val layoutResId: Int = R.layout.item_file,
    private val viewModel: FileBrowserViewModel
) : BaseAdapter<DocumentItem, ItemFileBinding>(
    layoutResId
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ItemFileBinding> = FileViewHolder(layoutResId, parent)

    inner class FileViewHolder(
        @LayoutRes private val layoutResId: Int,
        parent: ViewGroup
    ) : BaseViewHolder<ItemFileBinding>(
        layoutResId, parent
    ) {

        override fun onBind(item: Any?) {
            super.onBind(item)
            binding.vm = viewModel
            binding.documentItem = item as DocumentItem
        }
    }
}
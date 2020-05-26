package com.egiwon.scopedstorageexample.filebrowser

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.documentfile.provider.DocumentFile
import com.egiwon.scopedstorageexample.R
import com.egiwon.scopedstorageexample.base.BaseAdapter
import com.egiwon.scopedstorageexample.base.BaseViewHolder
import com.egiwon.scopedstorageexample.databinding.ItemFileBinding

class FileViewAdapter(
    @LayoutRes private val layoutResId: Int = R.layout.item_file
) : BaseAdapter<DocumentFile, ItemFileBinding>(
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
            binding.fileName = (item as? DocumentFile)?.name ?: return
            binding.fileType = (item as? DocumentFile)?.type ?: return
        }
    }
}
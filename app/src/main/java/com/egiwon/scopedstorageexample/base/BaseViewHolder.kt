package com.egiwon.scopedstorageexample.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<VDB : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int,
    parent: ViewGroup,
    private val bindingId: Int?
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
) {
    protected var binding: VDB = DataBindingUtil.bind(itemView)!!

    open fun onBind(item: Any?) {
        if (bindingId == null) return
        if (item == null) return

        binding.run { setVariable(bindingId, item) }
    }
}
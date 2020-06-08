package com.egiwon.scopedstorageexample.base

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<ANY : Any, VDB : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int
) : RecyclerView.Adapter<BaseViewHolder<VDB>>() {

    private val list = mutableListOf<ANY>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VDB> =
        object : BaseViewHolder<VDB>(layoutResId, parent) {}

    override fun onBindViewHolder(holder: BaseViewHolder<VDB>, position: Int) =
        holder.onBind(list[position])

    override fun getItemCount(): Int = list.size

    fun replaceItems(items: List<ANY>?) {
        if (items != null) {
            with(list) {
                clear()
                addAll(items)
            }
        }
    }
}
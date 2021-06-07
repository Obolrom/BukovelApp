package com.company.app.ui.misc

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class Adapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    @LayoutRes private val layoutResource: Int
) : ListAdapter<T, Adapter.BaseViewHolder<T>>(diffCallback) {
    interface OnClickItemListener<T> {
        fun onItemClick(item: T)
    }

    abstract class BaseViewHolder<T>(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}
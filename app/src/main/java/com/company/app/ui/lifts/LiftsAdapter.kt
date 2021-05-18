package com.company.app.ui.lifts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.company.app.R
import com.company.app.ui.map.Lift

class LiftsAdapter() :
        ListAdapter<Lift, LiftsAdapter.ViewHolder>(LiftComparator()) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(itemView: Lift) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_lift, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = getItem(position)
        holder.bind(itemView)
    }

    class LiftComparator : DiffUtil.ItemCallback<Lift>() {
        override fun areItemsTheSame(oldItem: Lift, newItem: Lift): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Lift, newItem: Lift): Boolean {
            return oldItem == newItem
        }
    }
}
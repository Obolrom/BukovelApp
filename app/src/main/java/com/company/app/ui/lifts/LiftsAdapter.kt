package com.company.app.ui.lifts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.company.app.R
import com.company.app.ui.map.Lift
import java.util.*

class LiftsAdapter(private val context: Context,
    private val callback: OnLiftClickListener) :
        ListAdapter<Lift, LiftsAdapter.ViewHolder>(LiftComparator()) {
    interface OnLiftClickListener {
        fun onLiftClick(lift: Lift)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: AppCompatTextView = itemView.findViewById(R.id.item_lift_tv)

        fun bind(lift: Lift) {
            textView.text = lift.name.toUpperCase(Locale.ROOT)
            // FIXME: 20.05.21 loading
//            textView.background = lift.
            if ( ! lift.active)
                textView.setBackgroundColor(ContextCompat
                    .getColor(context.applicationContext, R.color.light_grey))
            itemView.setOnClickListener {
                callback.onLiftClick(lift)
            }
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
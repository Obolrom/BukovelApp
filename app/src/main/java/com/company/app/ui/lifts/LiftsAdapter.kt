package com.company.app.ui.lifts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.company.app.R
import com.company.app.ui.map.Lift
import com.company.app.ui.misc.Adapter
import java.util.*

class LiftsAdapter(private val context: Context,
    private val callback: OnLiftClickListener
) : Adapter<Lift>(LiftComparator(), R.layout.item_lift) {
    interface OnLiftClickListener {
        fun onLiftClick(lift: Lift)
    }

    inner class LiftViewHolder(itemView: View) :
        Adapter.BaseViewHolder<Lift>(itemView) {
        private val textView: AppCompatTextView = itemView.findViewById(R.id.item_lift_tv)

        override fun bind(item: Lift) {
            textView.text = item.name.uppercase(Locale.ROOT)
            // FIXME: 20.05.21 loading
//            textView.background = lift.
            if ( ! item.active)
                textView.setBackgroundColor(ContextCompat
                    .getColor(context.applicationContext, R.color.light_grey))
            itemView.setOnClickListener {
                callback.onLiftClick(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Lift> {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_lift, parent, false)

        return LiftViewHolder(view)
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
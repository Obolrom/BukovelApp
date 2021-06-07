package com.company.app.ui.lifts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.company.app.R
import com.company.app.databinding.ItemLiftBinding
import com.company.app.ui.map.Lift
import com.company.app.ui.misc.Adapter
import java.util.*

class LiftsAdapter(private val context: Context,
    private val callback: OnLiftClickListener
) : Adapter<Lift>(LiftComparator(), R.layout.item_lift) {
    interface OnLiftClickListener {
        fun onLiftClick(lift: Lift)
    }

    inner class LiftViewHolder(private val binding: ItemLiftBinding) :
        Adapter.BaseViewHolder<Lift>(binding.root) {

        override fun bind(item: Lift) {
            binding.lift = item
            binding.executePendingBindings()

            // FIXME: 20.05.21 loading
//            textView.background = lift.
            if ( ! item.active)
                binding.itemLiftTv.setBackgroundColor(ContextCompat
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
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLiftBinding.inflate(layoutInflater, parent, false)

        return LiftViewHolder(binding)
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
package com.company.app.ui.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.DiffUtil
import com.company.app.R
import com.company.app.databinding.ItemServiceBinding
import com.company.app.ui.misc.Adapter

class ServiceAdapter(
    private val callback: OnClickItemListener<Service>
) : Adapter<Service>(ServiceComparator(), R.layout.item_service) {

    inner class ServiceViewHolder(private val binding: ItemServiceBinding) :
        Adapter.BaseViewHolder<Service>(binding.root) {

        override fun bind(item: Service) {
            binding.service = item
            binding.executePendingBindings()

            itemView.setOnClickListener {
                callback.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Service> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemServiceBinding.inflate(layoutInflater, parent, false)

        return ServiceViewHolder(binding)
    }

    class ServiceComparator : DiffUtil.ItemCallback<Service>() {
        override fun areItemsTheSame(oldItem: Service, newItem: Service): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Service, newItem: Service): Boolean {
            return oldItem == newItem
        }
    }
}
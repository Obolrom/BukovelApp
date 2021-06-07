package com.company.app.ui.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.DiffUtil
import com.company.app.R
import com.company.app.ui.misc.Adapter

class ServiceAdapter(
    private val callback: OnClickItemListener<Service>
) : Adapter<Service>(ServiceComparator(), R.layout.item_service) {

    inner class ServiceViewHolder(itemView: View) : Adapter.BaseViewHolder<Service>(itemView) {
        val image: ImageView = itemView.findViewById(R.id.service_iv)
        val name : TextView = itemView.findViewById(R.id.service_name_tv)
        val ratingBar: AppCompatRatingBar = itemView.findViewById(R.id.service_rb)

        override fun bind(item: Service) {
            name.text = item.title
            ratingBar.rating = item.score

            itemView.setOnClickListener {
                callback.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Service> {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_service, parent, false)

        return ServiceViewHolder(view)
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
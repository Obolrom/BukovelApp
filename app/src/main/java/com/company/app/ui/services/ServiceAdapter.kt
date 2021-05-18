package com.company.app.ui.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.company.app.R

class ServiceAdapter(private val callback: OnServiceClickListener) :
        ListAdapter<Service, ServiceAdapter.ViewHolder>(ServiceComparator()) {
    interface OnServiceClickListener {
        fun onServiceClicked(score: Float)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.service_iv)
        val name : TextView = itemView.findViewById(R.id.service_name_tv)
        val ratingBar: AppCompatRatingBar = itemView.findViewById(R.id.service_rb)

        fun bind(listItem: Service) {
            name.text = listItem.title
            ratingBar.rating = listItem.score

            itemView.setOnClickListener {
                callback.onServiceClicked(ratingBar.rating)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_service, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = getItem(position)
        holder.bind(listItem)
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
package com.company.app.ui.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.company.app.R
import java.lang.ClassCastException

class ServiceAdapter(callback: OnItemSelected?, services: List<Service>):
        RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {
    private val serviceList: List<Service> = services
    private var context: OnItemSelected? = null

    interface OnItemSelected {
        fun onItemClicked(index: Int)
    }

    init {
        try {
            context = callback
        } catch (exception: ClassCastException) {
            throw ClassCastException(exception.message + " RecyclerView.Adapter is not implemented")
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView
        val name : TextView
        var score: Double

        init {
            image = itemView.findViewById(R.id.service_iv)
            name = itemView.findViewById(R.id.service_name_tv)
            score = 0.0

            itemView.setOnClickListener {
                context?.onItemClicked(serviceList.indexOf(itemView.tag))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.fragment_service, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tag = serviceList[position]
//        holder.image.setImageResource(serviceList[position].background)
        holder.name.text = serviceList[position].name
        holder.score = serviceList[position].score
    }

    override fun getItemCount(): Int = serviceList.size
}
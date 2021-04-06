package com.company.app.ui.services

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.company.app.R

class ServicesFragment : Fragment(), ServiceAdapter.OnItemSelected {

    private lateinit var root: View

    private val services: List<Service> = listOf(
            Service(3.5f, "Ski school", 120932),
            Service(4.6f, "Хаски покатушки", 239012),
            Service(4.7f, "Whisky Bar", 120932),
            Service(3.89f, "Lords of the Boards", 239012),
            Service(2.5f, "Ski school", 120932),
            Service(2.6f, "Хаски покатушки", 239012),
            Service(2.7f, "Whisky Bar", 120932),
            Service(3.89f, "Lords of the Boards", 239012),
            Service(1.5f, "Ski school", 120932),
            Service(1.6f, "Хаски покатушки", 239012),
            Service(1.7f, "Whisky Bar", 120932),
            Service(1.89f, "Lords of the Boards", 239012))

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_services, container, false)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val recyclerView = root.findViewById<RecyclerView>(R.id.rv_services)
        recyclerView.setHasFixedSize(true)
        recyclerView.overScrollMode = View.OVER_SCROLL_ALWAYS

        val adapter = ServiceAdapter(this, services)
        recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
    }

    override fun onItemClicked(index: Int, score: Float) {
        context?.apply {
            Toast.makeText(context,
                    "index $index, score $score",
                    Toast.LENGTH_SHORT).show()
        }
    }
}
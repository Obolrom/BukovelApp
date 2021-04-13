package com.company.app.ui.services

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.company.app.R

class ServicesFragment : Fragment(), ServiceAdapter.OnServiceClickListener {

    private val serviceViewModel: ServiceViewModel by viewModels {
        ServiceViewModelFactory()
    }
    private lateinit var root: View

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
        val rvAdapter = ServiceAdapter(this)

        with(recyclerView, {
            setHasFixedSize(true)
            overScrollMode = View.OVER_SCROLL_ALWAYS
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
        })

        serviceViewModel.services.observe(viewLifecycleOwner, { items ->
            items?.let {
                rvAdapter.submitList(it)
                rvAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onServiceClicked(score: Float) {
        context?.let {
            Toast.makeText(context,
                "score $score",
                Toast.LENGTH_SHORT).show()
        }
    }
}
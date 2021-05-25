package com.company.app.ui.services

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.company.app.App
import com.company.app.R
import com.r0adkll.slidr.Slidr

class ServicesFragment : Fragment(), ServiceAdapter.OnServiceClickListener {

    private val serviceViewModel: ServiceViewModel by viewModels {
        ServiceViewModelFactory((activity?.application as App).repository)
    }
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var root: View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_services, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val recyclerView = root.findViewById<RecyclerView>(R.id.rv_services)
        val rvAdapter = ServiceAdapter(this)

        with(recyclerView, {
            setHasFixedSize(true)
            overScrollMode = View.OVER_SCROLL_ALWAYS
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
        })

        serviceViewModel.getServices().observe(viewLifecycleOwner, { items ->
            items?.let {
                rvAdapter.submitList(it)
                rvAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onServiceClicked(service: Service) {
        context?.let {
            sharedViewModel.currentServiceName.value = service.title
            findNavController().run {
                navigate(R.id.action_navigation_services_to_serviceDescriptionFragment)
            }
        }
    }
}
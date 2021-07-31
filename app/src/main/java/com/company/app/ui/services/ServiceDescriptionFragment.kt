package com.company.app.ui.services

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.company.app.App
import com.company.app.R


class ServiceDescriptionFragment : Fragment() {

    private lateinit var serviceViewModel: ServiceViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_service_description, container, false)

        return root
    }

    private fun displayReviews(reviews: List<ServiceReview>) {
        Toast.makeText(context?.applicationContext,
            reviews.toString(),
            Toast.LENGTH_LONG).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        serviceViewModel = ViewModelProvider(
//            viewModelStore,
//            ServiceViewModelFactory((activity?.application as App).repository)
//        ).get(ServiceViewModel::class.java)
//        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
//
//        sharedViewModel.currentServiceName.observe(viewLifecycleOwner, { serviceName ->
//            view.findViewById<TextView>(R.id.service_name_tv).text = serviceName
//            serviceViewModel.getServiceReviews(serviceName).observe(viewLifecycleOwner, {
//                displayReviews(it)
//            })
//        })
    }
}
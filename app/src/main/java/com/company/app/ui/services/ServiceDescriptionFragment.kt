package com.company.app.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.company.app.R
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrInterface


class ServiceDescriptionFragment : Fragment() {

    private lateinit var root: View
    private lateinit var slidrInterface: SlidrInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_service_description, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        fixme: how to implement slide back?
//        slidrInterface = Slidr.attach(requireActivity())
    }
}
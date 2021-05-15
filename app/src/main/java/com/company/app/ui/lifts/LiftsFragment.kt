package com.company.app.ui.lifts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.company.app.App
import com.company.app.R

class LiftsFragment : Fragment() {

    private val liftsViewModel: LiftsViewModel by viewModels {
        LiftsViewModelFactory((activity?.application as App).repository)
    }
    private lateinit var highLoadButton: AppCompatButton
    private lateinit var middleLoadButton: AppCompatButton
    private lateinit var lowLoadButton: AppCompatButton
    private lateinit var absentLoadButton: AppCompatButton

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_lifts, container, false)
        with(root) {
            highLoadButton = findViewById(R.id.high_load_lift_btn)
            middleLoadButton = findViewById(R.id.middle_load_lift_btn)
            lowLoadButton = findViewById(R.id.low_load_lift_btn)
            absentLoadButton = findViewById(R.id.absent_load_lift_btn)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        highLoadButton.setOnClickListener {
            Toast.makeText(context?.applicationContext,
                "high", Toast.LENGTH_SHORT).show()
        }
        middleLoadButton.setOnClickListener {

        }
        lowLoadButton.setOnClickListener {

        }
        absentLoadButton.setOnClickListener {
            Toast.makeText(context?.applicationContext,
                "absent", Toast.LENGTH_SHORT).show()
        }
    }
}
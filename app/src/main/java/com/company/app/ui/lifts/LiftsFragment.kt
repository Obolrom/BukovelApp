package com.company.app.ui.lifts

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.company.app.BukovelApp
import com.company.app.R
import com.company.app.ui.map.Lift

class LiftsFragment : Fragment(), LiftsAdapter.OnLiftClickListener {

//    private val liftsViewModel: LiftsViewModel by viewModels {
//        LiftsViewModelFactory((activity?.application as App).repository)
//    }
    private lateinit var liftRecyclerView: RecyclerView
    private lateinit var currentLiftRateTextView: AppCompatTextView
    private lateinit var rateBottomBar: LinearLayoutCompat
    private lateinit var activeLiftsAmount: AppCompatTextView
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
            liftRecyclerView = findViewById(R.id.rv_lifts)
            rateBottomBar = findViewById(R.id.lifts_status_rate_bottom_bar)
            activeLiftsAmount = findViewById(R.id.active_lifts)
            currentLiftRateTextView = findViewById(R.id.current_lift_rate)
            highLoadButton = findViewById(R.id.high_load_lift_btn)
            middleLoadButton = findViewById(R.id.middle_load_lift_btn)
            lowLoadButton = findViewById(R.id.low_load_lift_btn)
            absentLoadButton = findViewById(R.id.absent_load_lift_btn)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRV()
//        view.findViewById<AppCompatTextView>(R.id.lifts_amount).text =
//            liftsViewModel.lifts.value?.size.toString() ?: ""

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

    private fun initRV() {
        val adapter = LiftsAdapter(requireContext(), this)
//        with(liftRecyclerView) {
//            liftsViewModel.lifts.observe(viewLifecycleOwner, {
//                adapter.submitList(it)
//                adapter.notifyDataSetChanged()
//                activeLiftsAmount.text = it.map { lift -> lift.active }.size.toString()
//            })
//            this.adapter = adapter
//        }
    }

    override fun onLiftClick(lift: Lift) {
        currentLiftRateTextView.text = lift.name
        rateBottomBar.visibility = VISIBLE
    }
}
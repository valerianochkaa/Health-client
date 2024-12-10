package com.example.health.pages

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.health.R
import com.example.health.data.pressures.PressuresDTO
import com.example.health.databinding.FragmentPressureBinding
import com.example.health.utils.RetrofitClient
import com.example.health.utils.SwipeToDeleteCallback
import com.example.myhealth.ui.adapters.PressureAdapter
import kotlinx.coroutines.launch


class PressureFragment : Fragment(R.layout.fragment_pressure) {
    // View binding
    private var _binding: FragmentPressureBinding? = null
    private val binding get() = _binding!!

    // Recycler View
    private lateinit var adapter: PressureAdapter
    private var pressureList = mutableListOf<PressuresDTO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPressureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigation Component
        binding.btnBack.setOnClickListener{
            findNavController().navigate(R.id.pressure_to_diary)
        }

        // Recycler View
        binding.recycler.layoutManager = LinearLayoutManager(context)
        adapter = PressureAdapter(pressureList, context) { pressureId ->
            deletePressure(pressureId)
        }
        binding.recycler.adapter = adapter
        fetchPressures()

        // SetOnClickListener - btnAdd
        binding.btnAdd.setOnClickListener {
            addPressure()
            hideKeyboard()
        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val pressureId = pressureList[position].pressureId ?: return
                deletePressure(pressureId)
                adapter.notifyItemRemoved(position)
            }
        }

        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(binding.recycler)
    }

    private fun fetchPressures() {
        lifecycleScope.launch {
            try {
                val pressures = RetrofitClient.apiPressures.getAllPressures()
                pressureList.clear()
                pressureList.addAll(pressures)
                adapter.updateData(pressureList)
            } catch (e: Exception) {
                Log.e("PressureFragment", "Error fetching pressures", e)
            }
        }
    }

    private fun addPressure() {
        val newPressure = PressuresDTO(
            userIdPressure = 1,
            upperValue = 120,
            lowerValue = 80,
            pulseValue = 75,
            recordDate = "2023-10-01"
        )

        lifecycleScope.launch {
            try {
                val addedPressure = RetrofitClient.apiPressures.insertPressureAndGetId(newPressure)
                pressureList.add(addedPressure)
                adapter.updateData(pressureList)
            } catch (e: Exception) {
                Log.e("PressureFragment", "Error adding pressure", e)
            }
        }
    }

    private fun deletePressure(pressureId: Int) {
        lifecycleScope.launch {
            try {
                RetrofitClient.apiPressures.deletePressureById(pressureId)
                pressureList.removeAll { it.pressureId == pressureId }
                adapter.updateData(pressureList)
            } catch (e: Exception) {
                Log.e("PressureFragment", "Error deleting pressure", e)
            }
        }
    }


    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity?.currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
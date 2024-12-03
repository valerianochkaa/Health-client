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
import com.example.health.data.temperatures.TemperaturesDTO
import com.example.health.data.temperatures.TemperaturesList
import com.example.health.databinding.FragmentTemperatureBinding
import com.example.health.utils.RetrofitInstance
import com.example.health.utils.SwipeToDeleteCallback
import com.example.myhealth.ui.adapters.TemperatureAdapter
import kotlinx.coroutines.launch

class TemperatureFragment : Fragment(R.layout.fragment_temperature) {
    // View binding
    private var _binding: FragmentTemperatureBinding? = null
    private val binding get() = _binding!!

    // Recycler View
    private lateinit var adapter: TemperatureAdapter
    private var temperatureList = mutableListOf<TemperaturesDTO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTemperatureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigation Component
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.temperature_to_diary)
        }

        // Recycler View
        binding.recycler.layoutManager = LinearLayoutManager(context)
        adapter = TemperatureAdapter(temperatureList, context) { temperatureId ->
            deleteTemperature(temperatureId)
        }
        binding.recycler.adapter = adapter
        fetchTemperatures()

        // SetOnClickListener - btnAdd
        binding.btnAdd.setOnClickListener {
            addTemperature()
            hideKeyboard()
        }

        // SwipeToDeleteCallback
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val temperatureId = temperatureList[position].temperatureId ?: return
                deleteTemperature(temperatureId)
                adapter.notifyItemRemoved(position)
            }
        }
        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(binding.recycler)
    }

    private fun fetchTemperatures() {
        lifecycleScope.launch {
            try {
                val temperatures = RetrofitInstance.apiTemperatures.getAllTemperatures()
                temperatureList.clear()
                temperatureList.addAll(temperatures)
                adapter.updateData(temperatureList)
            } catch (e: Exception) {
                Log.e("TemperatureFragment", "Error fetching temperatures", e)
            }
        }
    }

    private fun addTemperature() {
        val newTemperature = TemperaturesDTO(userIdTemperature = 1, temperatureValue = 36.6, recordDate = "2023-10-01")

        lifecycleScope.launch {
            try {
                val addedTemperature = RetrofitInstance.apiTemperatures.insertTemperatureAndGetId(newTemperature)
                temperatureList.add(addedTemperature)
                adapter.updateData(temperatureList)
            } catch (e: Exception) {
                Log.e("TemperatureFragment", "Error adding temperature", e)
            }
        }
    }

    private fun deleteTemperature(temperatureId: Int) {
        lifecycleScope.launch {
            try {
                RetrofitInstance.apiTemperatures.deleteTemperatureById(temperatureId)
                temperatureList.removeAll { it.temperatureId == temperatureId }
                adapter.updateData(temperatureList)
            } catch (e: Exception) {
                Log.e("TemperatureFragment", "Error deleting temperature", e)
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
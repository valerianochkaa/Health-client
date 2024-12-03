package com.example.health.pages

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.health.R
import com.example.health.data.temperatures.TemperaturesList
import com.example.health.data.weights.WeightsList
import com.example.health.databinding.FragmentTemperatureBinding
import com.example.health.utils.SwipeToDeleteCallback
import com.example.myhealth.ui.adapters.TemperatureAdapter
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

class TemperatureFragment : Fragment(R.layout.fragment_temperature) {
    // View binding
    private var _binding: FragmentTemperatureBinding? = null
    private val binding get() = _binding!!

    // Recycler View
    private var temperaturesList = ArrayList<TemperaturesList>()
    private lateinit var adapter: TemperatureAdapter

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
        binding.btnBack.setOnClickListener{
            findNavController().navigate(R.id.temperature_to_diary)
        }

        // Recycler View
        binding.recycler.layoutManager = LinearLayoutManager(context)
        addDataToList()
        adapter = TemperatureAdapter(temperaturesList, context)
        binding.recycler.adapter = adapter

        binding.btnAdd.setOnClickListener {
            addTemperature()
            hideKeyboard()
        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                temperaturesList.removeAt(position)
                binding.recycler.adapter?.notifyItemRemoved(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recycler)
    }

    private fun addDataToList() {
        temperaturesList.add(TemperaturesList(36.6,"01 января 2024, 8:00"))
        temperaturesList.add(TemperaturesList(36.4,"02 января 2024, 8:10"))
    }

    private fun addTemperature() {
        val temperatureInput = binding.editValue.text.toString()

        if (temperatureInput.isEmpty()) {
            Toast.makeText(context, "Введите значение", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val currentDate = SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.getDefault()).format(
                Date()
            )

            temperaturesList.add(TemperaturesList(temperatureInput.toDouble(), currentDate))
            adapter.notifyItemInserted(temperaturesList.size - 1)

            binding.editValue.text.clear()
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Некорректный ввод", Toast.LENGTH_SHORT).show()
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
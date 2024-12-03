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
import com.example.health.data.pressures.PressuresList
import com.example.health.data.temperatures.TemperaturesList
import com.example.health.data.weights.WeightsList
import com.example.health.databinding.FragmentPressureBinding
import com.example.health.utils.SwipeToDeleteCallback
import com.example.myhealth.ui.adapters.PressureAdapter
import com.example.myhealth.ui.adapters.TemperatureAdapter
import com.example.myhealth.ui.adapters.WeightAdapter
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale


class PressureFragment : Fragment(R.layout.fragment_pressure) {
    // View binding
    private var _binding: FragmentPressureBinding? = null
    private val binding get() = _binding!!

    // Recycler View
    private var pressuresList = ArrayList<PressuresList>()
    private lateinit var adapter: PressureAdapter

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
        addDataToList()
        adapter = PressureAdapter(pressuresList, context)
        binding.recycler.adapter = adapter

        binding.btnAdd.setOnClickListener {
            addPressure()
            hideKeyboard()
        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                pressuresList.removeAt(position)
                binding.recycler.adapter?.notifyItemRemoved(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recycler)
    }

    private fun addDataToList() {
        pressuresList.add(PressuresList(120,80,72,"01 января 2024, 8:00"))
        pressuresList.add(PressuresList(115,75,68,"02 января 2024, 8:10"))
    }

    private fun addPressure() {
        val upperInput = binding.editValue1.text.toString()
        val lowerInput = binding.editValue2.text.toString()
        val pulseInput = binding.editValue3.text.toString()

        if (upperInput.isEmpty() || lowerInput.isEmpty() || pulseInput.isEmpty()) {
            Toast.makeText(context, "Введите все значение", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val currentDate = SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.getDefault()).format(
                Date()
            )

            pressuresList.add(PressuresList(upperInput.toInt(), lowerInput.toInt(), pulseInput.toInt(), currentDate))
            adapter.notifyItemInserted(pressuresList.size - 1)

            binding.editValue1.text.clear()
            binding.editValue2.text.clear()
            binding.editValue3.text.clear()
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
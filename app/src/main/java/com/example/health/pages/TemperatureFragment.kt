package com.example.health.pages

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.health.R
import com.example.health.data.temperatures.TemperaturesDTO
import com.example.health.databinding.FragmentTemperatureBinding
import com.example.health.utils.RetrofitClient
import com.example.health.utils.SwipeToDeleteCallback
import com.example.myhealth.ui.adapters.TemperatureAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TemperatureFragment : Fragment(R.layout.fragment_temperature) {
    private var _binding: FragmentTemperatureBinding? = null
    private val binding get() = _binding!!
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
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.temperature_to_diary)
        }
        binding.recycler.layoutManager = LinearLayoutManager(context)
        adapter = TemperatureAdapter(temperatureList, context) { temperatureId ->
            deleteTemperature(temperatureId)
        }
        binding.recycler.adapter = adapter
        fetchTemperatures()
        binding.btnAdd.setOnClickListener {
            addTemperature()
            hideKeyboard()
        }
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
                val temperatures = RetrofitClient.temperaturesApi.getAllTemperatures()
                temperatureList.clear()
                temperatureList.addAll(temperatures)
                adapter.updateData(temperatureList)
            } catch (e: Exception) {
                Log.e("TemperatureFragment", "Error fetching temperatures", e)
                Toast.makeText(context, "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addTemperature() {
        val temperatureInput = binding.editValue.text.toString()
        if (temperatureInput.isEmpty()) {
            Toast.makeText(context, "Введите температуру", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val currentDate = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("ru", "RU")).format(Date())
            val newTemperature = TemperaturesDTO(
                userIdTemperature = 1,
                temperatureValue = temperatureInput.toDouble(),
                recordDate = currentDate
            )
            val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null)
            if (token.isNullOrEmpty()) {
                Toast.makeText(context, "Токен отсутствует. Авторизуйтесь снова.", Toast.LENGTH_SHORT).show()
                return
            }
            Log.d("TemperatureFragment", "Sending temperature: $newTemperature with token: $token")
            lifecycleScope.launch {
                try {
                    val addedTemperature = RetrofitClient.temperaturesApi.insertTemperatureAndGetId(newTemperature, "Bearer $token")
                    temperatureList.add(addedTemperature)
                    adapter.updateData(temperatureList)
                    binding.editValue.text.clear()
                } catch (e: Exception) {
                    Log.e("TemperatureFragment", "Error adding temperature", e)
                    Toast.makeText(context, "Ошибка при добавлении температуры", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Некорректный ввод", Toast.LENGTH_SHORT).show()
        }
    }


    private fun deleteTemperature(temperatureId: Int) {
        lifecycleScope.launch {
            try {
                RetrofitClient.temperaturesApi.deleteTemperatureById(temperatureId)
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
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
import com.example.health.data.pressures.PressuresDTO
import com.example.health.databinding.FragmentPressureBinding
import com.example.health.utils.RetrofitClient
import com.example.health.utils.SwipeToDeleteCallback
import com.example.myhealth.ui.adapters.PressureAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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
                // Запрос на сервер для получения всех записей давления
                val pressures = RetrofitClient.pressuresApi.getAllPressures()
                pressureList.clear() // Очистка текущего списка
                pressureList.addAll(pressures) // Добавление новых данных
                adapter.updateData(pressureList) // Обновление данных в адаптере
            } catch (e: Exception) {
                Log.e("PressureFragment", "Error fetching pressures", e)
                Toast.makeText(context, "Ошибка при загрузке данных давления", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addPressure() {
        val upperValueInput = binding.editValue1.text.toString()
        val lowerValueInput = binding.editValue2.text.toString()
        val pulseValueInput = binding.editValue3.text.toString()

        if (upperValueInput.isEmpty() || lowerValueInput.isEmpty() || pulseValueInput.isEmpty()) {
            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Форматирование текущей даты
            val currentDate = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("ru", "RU")).format(
                Date()
            )

            // Создание нового объекта давления
            val newPressure = PressuresDTO(
                userIdPressure = 1, // ID пользователя (заменить на актуальный)
                upperValue = upperValueInput.toInt(), // Верхнее значение давления
                lowerValue = lowerValueInput.toInt(), // Нижнее значение давления
                pulseValue = pulseValueInput.toInt(), // Пульс
                recordDate = currentDate
            )

            // Получение токена из SharedPreferences
            val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null)

            if (token.isNullOrEmpty()) {
                Toast.makeText(context, "Токен отсутствует. Авторизуйтесь снова.", Toast.LENGTH_SHORT).show()
                return
            }

            Log.d("PressureFragment", "Sending pressure: $newPressure with token: $token")

            lifecycleScope.launch {
                try {
                    // Отправка новой записи давления на сервер и получение добавленного объекта
                    val addedPressure = RetrofitClient.pressuresApi.insertPressureAndGetId(newPressure, "Bearer $token")
                    pressureList.add(addedPressure) // Добавление новой записи в список
                    adapter.updateData(pressureList) // Обновление данных в адаптере

                    // Очистка полей ввода после успешного добавления
                    binding.editValue1.text.clear()
                    binding.editValue2.text.clear()
                    binding.editValue3.text.clear()
                } catch (e: Exception) {
                    Log.e("PressureFragment", "Error adding pressure", e)
                    Toast.makeText(context, "Ошибка при добавлении записи давления", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Некорректный ввод значений", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deletePressure(pressureId: Int) {
        lifecycleScope.launch {
            try {
                RetrofitClient.pressuresApi.deletePressureById(pressureId)
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
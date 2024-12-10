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
import com.example.health.data.weights.WeightsDTO
import com.example.health.databinding.FragmentWeightBinding
import com.example.health.utils.RetrofitClient
import com.example.health.utils.SwipeToDeleteCallback
import com.example.myhealth.ui.adapters.WeightAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeightFragment : Fragment(R.layout.fragment_weight) {
    private var _binding: FragmentWeightBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: WeightAdapter
    private val weightList = mutableListOf<WeightsDTO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.weight_to_diary)
        }
        binding.recycler.layoutManager = LinearLayoutManager(context)
        adapter = WeightAdapter(weightList, context) { weightId -> deleteWeight(weightId)}
        binding.recycler.adapter = adapter
        fetchWeights()
        binding.btnAdd.setOnClickListener {
            addWeight()
            hideKeyboard()
        }
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val weightId = weightList[position].weightId ?: return
                deleteWeight(weightId)
                adapter.notifyItemRemoved(position)
            }
        }
        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(binding.recycler)
    }
    private fun fetchWeights() {
        lifecycleScope.launch {
            try {
                val weights = RetrofitClient.weightsApi.getAllWeights()
                weightList.clear()
                weightList.addAll(weights)
                adapter.updateData(weightList)
            } catch (e: Exception) {
                Log.e("WeightFragment", "Error fetching weights", e)
            }
        }
    }
    private fun addWeight() {
        val weightInput = binding.editValue.text.toString()
        if (weightInput.isEmpty()) {
            Toast.makeText(context, "Введите вес", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val currentDate = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("ru", "RU")).format(Date())
            val newWeight = WeightsDTO(
                userIdWeights = 1,
                weightValue = weightInput.toDouble(),
                recordDate = currentDate
            )
            Log.d("WeightFragment", "Sending weight: $newWeight")
            lifecycleScope.launch {
                try {
                    val addedWeight = RetrofitClient.weightsApi.insertWeightAndGetId(newWeight)
                    weightList.add(addedWeight)
                    adapter.updateData(weightList)
                    binding.editValue.text.clear()
                } catch (e: Exception) {
                    Log.e("WeightFragment", "Error adding weight", e)
                    Toast.makeText(context, "Ошибка при добавлении веса", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Некорректный ввод", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteWeight(weightId: Int) {
        lifecycleScope.launch {
            try {
                RetrofitClient.weightsApi.deleteWeightById(weightId)
                weightList.removeAll { it.weightId == weightId }
                adapter.updateData(weightList)
            } catch (e: Exception) {
                Log.e("WeightFragment", "Error deleting weight", e)
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
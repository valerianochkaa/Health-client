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
import com.example.health.data.weights.WeightsList
import com.example.health.databinding.FragmentWeightBinding
import com.example.health.utils.SwipeToDeleteCallback
import com.example.myhealth.ui.adapters.WeightAdapter
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

class WeightFragment : Fragment(R.layout.fragment_weight) {
    // View binding
    private var _binding: FragmentWeightBinding? = null
    private val binding get() = _binding!!

    // Recycler View
    private var weightList = ArrayList<WeightsList>()
    private lateinit var adapter: WeightAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigation Component
        binding.btnBack.setOnClickListener{
            findNavController().navigate(R.id.weight_to_diary)
        }

        // Recycler View
        binding.recycler.layoutManager = LinearLayoutManager(context)
        addDataToList()
        adapter = WeightAdapter(weightList, context)
        binding.recycler.adapter = adapter

        binding.btnAdd.setOnClickListener {
            addWeight()
            hideKeyboard()
        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                weightList.removeAt(position)
                binding.recycler.adapter?.notifyItemRemoved(position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recycler)
    }

    private fun addDataToList() {
        weightList.add(WeightsList(55.0,"01 января 2024, 8:00"))
        weightList.add(WeightsList(55.5,"02 января 2024, 8:10"))
    }

    private fun addWeight() {
        val weightInput = binding.editValue.text.toString()

        if (weightInput.isEmpty()) {
            Toast.makeText(context, "Введите вес", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val currentDate = SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.getDefault()).format(Date())

            weightList.add(WeightsList(weightInput.toDouble(), currentDate))
            adapter.notifyItemInserted(weightList.size - 1)

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
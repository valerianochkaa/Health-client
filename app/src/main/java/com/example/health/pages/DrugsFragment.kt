package com.example.health.pages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.health.R
import com.example.health.data.drugs.DrugsDTO
import com.example.health.databinding.FragmentDrugsBinding
import com.example.health.utils.RetrofitInstance
import com.example.myhealth.ui.adapters.DrugsAdapter
import kotlinx.coroutines.launch

class DrugsFragment : Fragment(R.layout.fragment_drugs) {
    // View binding
    private var _binding: FragmentDrugsBinding? = null
    private val binding get() = _binding!!
    // Recycler View
    private var drugsList = mutableListOf<DrugsDTO>()
    private lateinit var adapter: DrugsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDrugsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigation Component
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.drugs_to_drugs_category)
        }

        val categoryName = requireArguments().getString("categoryName")
        binding.textCategoryName.text = categoryName

        adapter = DrugsAdapter(emptyList(), requireContext(), this)
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = adapter

        fetchDrugs(categoryName)
    }

    private fun fetchDrugs(categoryName: String?) {
        lifecycleScope.launch {
            try {
                val drugs = RetrofitInstance.drugsApi.getAllDrugs()
                drugsList.clear()
                drugsList.addAll(drugs)

                val filteredDrugsList = when (categoryName) {
                    "Таблетки" -> drugsList.filter { it.drugCategoryId == 1 }
                    "Спреи" -> drugsList.filter { it.drugCategoryId == 2 }
                    "Мази" -> drugsList.filter { it.drugCategoryId == 3 }
                    "Уколы" -> drugsList.filter { it.drugCategoryId == 4 }
                    else -> emptyList()
                }

                adapter.updateData(filteredDrugsList)
            } catch (e: Exception) {
                Log.e("DrugsFragment", "Error fetching drugs", e)
            }
        }
    }
}

//    private fun filterList(query: String?) {
//        if (query != null) {
//            val filteredList = filteredDrugsList.filter {
//                it.name.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
//            }
//
//            if (filteredList.isEmpty()) {
//                binding.textBlank.visibility = View.VISIBLE
//                binding.recycler.visibility = View.GONE
//            } else {
//                binding.textBlank.visibility = View.GONE
//                binding.recycler.visibility = View.VISIBLE
//                adapter.setFilteredList(filteredList)
//            }
//        }


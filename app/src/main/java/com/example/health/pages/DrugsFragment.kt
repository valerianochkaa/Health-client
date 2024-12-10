package com.example.health.pages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.health.R
import com.example.health.data.drugLike.DrugWithLikeStatus
import com.example.health.data.drugs.DrugsDTO
import com.example.health.databinding.FragmentDrugsBinding
import com.example.health.utils.RetrofitInstance
import com.example.myhealth.ui.adapters.DrugsAdapter
import kotlinx.coroutines.launch
import java.util.Locale

class DrugsFragment : Fragment(R.layout.fragment_drugs) {
    private var _binding: FragmentDrugsBinding? = null
    private val binding get() = _binding!!

    private var allDrugsList = mutableListOf<DrugWithLikeStatus>()
    private var filteredDrugsList = mutableListOf<DrugWithLikeStatus>()
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

        // Recycler View
        adapter = DrugsAdapter(emptyList(), requireContext(), this)
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = adapter
        fetchDrugs(categoryName)

        // Search View
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(query: String?) {
        if (query != null && query.isNotEmpty()) {
            val filteredList = allDrugsList.filter { drugWithLikeStatus ->
                drugWithLikeStatus.drug.drugName.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
            }

            if (filteredList.isEmpty()) {
                binding.textBlank.visibility = View.VISIBLE
                binding.recycler.visibility = View.GONE
            } else {
                binding.textBlank.visibility = View.GONE
                binding.recycler.visibility = View.VISIBLE
                adapter.updateData(filteredList)
            }
        } else {
            binding.textBlank.visibility = View.GONE
            binding.recycler.visibility = View.VISIBLE
            adapter.updateData(allDrugsList)
        }
    }

    private fun fetchDrugs(categoryName: String?) {
        lifecycleScope.launch {
            try {
                val userId = 1 // Замените на актуальный идентификатор пользователя
                val likedDrugIds = getLikedDrugIds(userId)

                val drugs = RetrofitInstance.drugsApi.getAllDrugs()
                allDrugsList.clear()
                allDrugsList.addAll(drugs.map { drug ->
                    DrugWithLikeStatus(drug, likedDrugIds.contains(drug.drugId))
                })

                filteredDrugsList.clear()
                filteredDrugsList.addAll(allDrugsList.filter { drugWithLikeStatus ->
                    when (categoryName) {
                        "Таблетки" -> drugWithLikeStatus.drug.drugCategoryId == 1
                        "Спреи" -> drugWithLikeStatus.drug.drugCategoryId == 2
                        "Мази" -> drugWithLikeStatus.drug.drugCategoryId == 3
                        "Уколы" -> drugWithLikeStatus.drug.drugCategoryId == 4
                        else -> true // Если категория не распознана, показываем все лекарства
                    }
                })

                adapter.updateData(filteredDrugsList)
            } catch (e: Exception) {
                Log.e("DrugsFragment", "Error fetching drugs", e)
            }
        }
    }

    suspend fun getLikedDrugIds(userId: Int): Set<Int> {
        return try {
            val likedDrugs = RetrofitInstance.drugLikeApi.getDrugLikesByUser(userId)
            likedDrugs.map { it.drugId }.toSet()
        } catch (e: Exception) {
            Log.e("DrugsFragment", "Error fetching liked drug ids", e)
            emptySet()
        }
    }
}





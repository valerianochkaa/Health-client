package com.example.health.pages

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.health.R
import com.example.health.data.drugLike.DrugWithLikeStatus
import com.example.health.databinding.FragmentDrugsBinding
import com.example.health.utils.RetrofitClient
import com.example.myhealth.ui.adapters.DrugsAdapter
import kotlinx.coroutines.launch

class DrugsFragment : Fragment(R.layout.fragment_drugs) {
    private var _binding: FragmentDrugsBinding? = null
    private val binding get() = _binding!!
    private val allDrugsList = mutableListOf<DrugWithLikeStatus>()
    private val filteredDrugsList = mutableListOf<DrugWithLikeStatus>()
    private lateinit var adapter: DrugsAdapter
    private val userId = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrugsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupRecyclerView()
        setupSearchView()
        val categoryName = requireArguments().getString("categoryName")
        binding.textCategoryName.text = categoryName
        fetchDrugs(categoryName)
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.drugs_to_drugs_category)
        }
    }

    private fun setupRecyclerView() {
        // Получаем токен авторизации из SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        if (token.isNullOrEmpty()) {
            Toast.makeText(context, "Токен отсутствует. Авторизуйтесь снова.", Toast.LENGTH_SHORT).show()
            return
        }

        // Используем готовый экземпляр API из RetrofitClient
        val drugLikeApi = RetrofitClient.drugLikeApi // Или создайте отдельный DrugLikeApi, если это другой интерфейс

        // Создаем адаптер с передачей необходимых параметров
        adapter = DrugsAdapter(
            drugsList = allDrugsList, // Список данных
            context = requireContext(), // Контекст
            lifecycleOwner = viewLifecycleOwner, // LifecycleOwner для корутин
            drugLikeApi = drugLikeApi, // Экземпляр API
            token = "Bearer $token" // Передаем токен с приставкой "Bearer"
        )

        // Настройка RecyclerView
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = adapter
    }



    private fun setupSearchView() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?) = filterList(newText).let { true }
        })
    }

    private fun filterList(query: String?) {
        val filteredList = if (!query.isNullOrEmpty()) {
            filteredDrugsList.filter { it.drug.drugName.contains(query, ignoreCase = true) }
        } else {
            filteredDrugsList
        }
        updateUI(filteredList)
    }

    private fun updateUI(filteredList: List<DrugWithLikeStatus>) {
        val isEmpty = filteredList.isEmpty()
        binding.textBlank.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recycler.visibility = if (isEmpty) View.GONE else View.VISIBLE
        adapter.updateData(filteredList)
    }

    private fun fetchDrugs(categoryName: String?) {
        lifecycleScope.launch {
            try {
                val likedDrugIds = getLikedDrugIds(userId)
                val drugs = RetrofitClient.drugsApi.getAllDrugs()
                allDrugsList.clear()
                allDrugsList.addAll(drugs.map { DrugWithLikeStatus(it, likedDrugIds.contains(it.drugId)) })
                filteredDrugsList.clear()
                filteredDrugsList.addAll(allDrugsList.filter { drugWithLikeStatus ->
                    when (categoryName) {
                        "Таблетки" -> drugWithLikeStatus.drug.drugCategoryId == 1
                        "Спреи" -> drugWithLikeStatus.drug.drugCategoryId == 2
                        "Мази" -> drugWithLikeStatus.drug.drugCategoryId == 3
                        "Уколы" -> drugWithLikeStatus.drug.drugCategoryId == 4
                        else -> true
                    }
                })
                adapter.updateData(filteredDrugsList)
            } catch (e: Exception) {
                Log.e("DrugsFragment", "Error fetching drugs", e)
            }
        }
    }

    private suspend fun getLikedDrugIds(userId: Int): Set<Int> {
        return try {
            RetrofitClient.drugLikeApi.getDrugLikesByUser(userId).map { it.drugId }.toSet()
        } catch (e: Exception) {
            Log.e("DrugsFragment", "Error fetching liked drug ids", e)
            emptySet()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





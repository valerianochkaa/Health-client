package com.example.health.pages

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.health.R
import com.example.health.data.drugLike.DrugWithLikeStatus
import com.example.health.data.drugs.DrugsDTO
import com.example.health.databinding.FragmentLikesBinding
import com.example.health.utils.RetrofitClient
import com.example.health.utils.RetrofitClient.drugLikeApi
import com.example.myhealth.ui.adapters.DrugsAdapter
import kotlinx.coroutines.launch

class LikesFragment : Fragment(R.layout.fragment_likes) {
    private var _binding: FragmentLikesBinding? = null
    private val binding get() = _binding!!
    private val likedDrugsList = mutableListOf<DrugWithLikeStatus>()
    private lateinit var adapter: DrugsAdapter
    private val userId = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchLikedDrugs()
    }

    private fun setupRecyclerView() {
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        if (token.isNullOrEmpty()) {
            Toast.makeText(context, "Токен отсутствует. Авторизуйтесь снова.", Toast.LENGTH_SHORT).show()
            return
        }
        val drugLikeApi = RetrofitClient.drugLikeApi
        adapter = DrugsAdapter(
            drugsList = likedDrugsList,
            context = requireContext(),
            lifecycleOwner = viewLifecycleOwner,
            drugLikeApi = drugLikeApi,
            token = "Bearer $token"
        )
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = adapter
    }

    private fun fetchLikedDrugs() {
        viewLifecycleOwner.lifecycleScope.launch {
            runCatching {
                RetrofitClient.drugLikeApi.getDrugLikesByUser(userId)
            }.onSuccess { likedDrugs ->
                val token = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    .getString("token", null)

                if (token.isNullOrEmpty()) {
                    Toast.makeText(context, "Токен отсутствует. Авторизуйтесь снова.", Toast.LENGTH_SHORT).show()
                    return@onSuccess
                }
                val drugDetailsList = likedDrugs.mapNotNull { likedDrug ->
                    fetchDrugDetails(likedDrug.drugId)
                }
                likedDrugsList.clear()
                likedDrugsList.addAll(drugDetailsList.map { DrugWithLikeStatus(it, true) })
                adapter.notifyDataSetChanged()
            }.onFailure { e ->
                Log.e("LikesFragment", "Ошибка при загрузке понравившихся препаратов", e)
            }
        }
    }

    private suspend fun fetchDrugDetails(drugId: Int): DrugsDTO? {
        return runCatching {
            RetrofitClient.drugsApi.getDrugById(drugId)
        }.getOrNull()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


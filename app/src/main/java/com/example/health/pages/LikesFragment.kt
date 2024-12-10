package com.example.health.pages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var drugsList = mutableListOf<DrugWithLikeStatus>()
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
//        setupRecyclerView()
        fetchLikedDrugs()
    }

//    private fun setupRecyclerView() {
//        adapter = DrugsAdapter(drugsList, requireContext(), viewLifecycleOwner)
//        binding.recycler.layoutManager = LinearLayoutManager(context)
//        binding.recycler.adapter = adapter
//    }

    private fun fetchLikedDrugs() {
        viewLifecycleOwner.lifecycleScope.launch {
            runCatching {
                drugLikeApi.getDrugLikesByUser(userId)
            }.onSuccess { likedDrugs ->
                val drugDetailsList = likedDrugs.mapNotNull { likedDrug ->
                    fetchDrugDetails(likedDrug.drugId)
                }
                drugsList.clear()
                drugsList.addAll(drugDetailsList.map { DrugWithLikeStatus(it, true) })
                adapter.notifyDataSetChanged()
            }.onFailure { e ->
                Log.e("LikesFragment", "Error fetching liked drugs", e)
            }
        }
    }

    private suspend fun fetchDrugDetails(drugId: Int): DrugsDTO? {
        return runCatching {
            RetrofitClient.drugsApi.getDrugById(drugId)
        }.onFailure { e ->
            Log.e("LikesFragment", "Error fetching drug details for ID $drugId", e)
        }.getOrNull()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


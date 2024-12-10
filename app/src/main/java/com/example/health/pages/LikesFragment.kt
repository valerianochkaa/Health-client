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
import com.example.health.data.drugLike.DrugLikeApi
import com.example.health.data.drugs.DrugsDTO
import com.example.health.data.drugs.DrugsList
import com.example.health.databinding.FragmentLikesBinding
import com.example.health.utils.RetrofitInstance
import com.example.health.utils.RetrofitInstance.drugLikeApi
import com.example.myhealth.ui.adapters.DrugsAdapter
import kotlinx.coroutines.launch
import java.util.ArrayList

class LikesFragment : Fragment(R.layout.fragment_likes) {
    // View binding
    private var _binding: FragmentLikesBinding? = null
    private val binding get() = _binding!!

    // Recycler View
    private var drugsList = ArrayList<DrugsDTO>()
    private lateinit var adapter: DrugsAdapter
    private val userId = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLikesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // RecyclerView
        adapter = DrugsAdapter(drugsList, requireContext(), viewLifecycleOwner)
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = adapter
        fetchLikedDrugs()
    }
    private fun fetchLikedDrugs() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val likedDrugs = drugLikeApi.getDrugLikesByUser(userId)
                for (likedDrug in likedDrugs) {
                    val drugDetails = fetchDrugDetails(likedDrug.drugId)
                    if (drugDetails != null) {
                        drugsList.add(drugDetails)
                    }
                }
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("LikesFragment", "Error fetching liked drugs", e)
            }
        }
    }
    private suspend fun fetchDrugDetails(drugId: Int): DrugsDTO? {
        return try {
            val drugApiResponse = RetrofitInstance.drugsApi.getDrugById(drugId)
            drugApiResponse
        } catch (e: Exception) {
            Log.e("LikesFragment", "Error fetching drug details for ID $drugId", e)
            null
        }
    }
}


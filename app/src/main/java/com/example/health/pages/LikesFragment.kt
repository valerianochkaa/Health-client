package com.example.health.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.health.R
import com.example.health.data.drugs.DrugsList
import com.example.health.databinding.FragmentLikesBinding
import com.example.myhealth.ui.adapters.DrugsAdapter
import java.util.ArrayList

class LikesFragment : Fragment(R.layout.fragment_likes) {
    // View binding
    private var _binding: FragmentLikesBinding? = null
    private val binding get() = _binding!!

    // Recycler View
    private var drugsList = ArrayList<DrugsList>()
    private lateinit var adapter: DrugsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLikesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Recycler View
        binding.recycler.layoutManager = LinearLayoutManager(context)
        addDataToList()
        adapter = DrugsAdapter(drugsList.filter { it.like }, context)
        binding.recycler.adapter = adapter
    }

    private fun addDataToList() {
        drugsList.add(
            DrugsList("таблетка","таблетки", true,"описание",
            "действующие вещества", "состав",
            "показания","противопоказания",
            "побочные эффекты","передозировка",
            "условия хранения",true, 150.0, "аналог")
        )
        drugsList.add(
            DrugsList("спрей","таблетки", true,"описание",
                "действующие вещества", "состав",
                "показания","противопоказания",
                "побочные эффекты","передозировка",
                "условия хранения",true, 150.0, "аналог")
        )
        drugsList.add(
            DrugsList("мазь","таблетки", false,"описание",
                "действующие вещества", "состав",
                "показания","противопоказания",
                "побочные эффекты","передозировка",
                "условия хранения",true, 150.0, "аналог")
        )
        drugsList.add(
            DrugsList("укол","таблетки", false,"описание",
                "действующие вещества", "состав",
                "показания","противопоказания",
                "побочные эффекты","передозировка",
                "условия хранения",true, 150.0, "аналог")
        )
    }
}

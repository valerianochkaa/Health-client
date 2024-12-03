package com.example.health.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.health.R
import com.example.health.data.drugs.DrugsList
import com.example.health.data.temperatures.TemperaturesList
import com.example.health.databinding.FragmentDrugsBinding
import com.example.myhealth.ui.adapters.DrugsAdapter
import com.example.myhealth.ui.adapters.TemperatureAdapter
import java.util.ArrayList
import java.util.Locale

class DrugsFragment : Fragment(R.layout.fragment_drugs) {
    // View binding
    private var _binding: FragmentDrugsBinding? = null
    private val binding get() = _binding!!

    // Recycler View
    private var drugsList = ArrayList<DrugsList>()
    private lateinit var adapter: DrugsAdapter
    private lateinit var filteredDrugsList: List<DrugsList>

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
        binding.btnBack.setOnClickListener{
            findNavController().navigate(R.id.drugs_to_drugs_category)
        }
        val categoryName = requireArguments().getString("categoryName")
        binding.textCategoryName.text = categoryName

        // Recycler View
        binding.recycler.layoutManager = LinearLayoutManager(context)
        filteredDrugsList = when (categoryName) {
            "Таблетки" -> {
                addDataToList()
                drugsList.filter { it.categoryName == "таблетка" }
            }
            "Спреи" -> {
                addDataToList()
                drugsList.filter { it.categoryName == "спрей" }
            }
            "Мази" -> {
                addDataToList()
                drugsList.filter { it.categoryName == "мазь" }
            }
            "Уколы" -> {
                addDataToList()
                drugsList.filter { it.categoryName == "укол" }
            }
            else -> emptyList()
        }

        adapter = DrugsAdapter(filteredDrugsList, context)
        binding.recycler.adapter = adapter

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

    private fun addDataToList() {
        drugsList.add(
            DrugsList(
                "таблетка1", "таблетка", true, "описание",
                "действующие вещества", "состав",
                "показания", "противопоказания",
                "побочные эффекты", "передозировка",
                "условия хранения", true, 150.0, "аналог"
            )
        )
        drugsList.add(
            DrugsList(
                "спрей1", "спрей", true, "описание",
                "действующие вещества", "состав",
                "показания", "противопоказания",
                "побочные эффекты", "передозировка",
                "условия хранения", true, 150.0, "аналог"
            )
        )
        drugsList.add(
            DrugsList(
                "таблетка11", "таблетка", true, "описание",
                "действующие вещества", "состав",
                "показания", "противопоказания",
                "побочные эффекты", "передозировка",
                "условия хранения", true, 150.0, "аналог"
            )
        )
        drugsList.add(
            DrugsList(
                "таблетка2", "таблетка", true, "описание",
                "действующие вещества", "состав",
                "показания", "противопоказания",
                "побочные эффекты", "передозировка",
                "условия хранения", true, 150.0, "аналог"
            )
        )
        drugsList.add(
            DrugsList(
                "таблетка3", "таблетка", true, "описание",
                "действующие вещества", "состав",
                "показания", "противопоказания",
                "побочные эффекты", "передозировка",
                "условия хранения", true, 150.0, "аналог"
            )
        )

        drugsList.add(
            DrugsList(
                "спрей11", "спрей", true, "описание",
                "действующие вещества", "состав",
                "показания", "противопоказания",
                "побочные эффекты", "передозировка",
                "условия хранения", true, 150.0, "аналог"
            )
        )
        drugsList.add(
            DrugsList(
                "спрей2", "спрей", true, "описание",
                "действующие вещества", "состав",
                "показания", "противопоказания",
                "побочные эффекты", "передозировка",
                "условия хранения", true, 150.0, "аналог"
            )
        )
        drugsList.add(
            DrugsList(
                "спрей3", "спрей", true, "описание",
                "действующие вещества", "состав",
                "показания", "противопоказания",
                "побочные эффекты", "передозировка",
                "условия хранения", true, 150.0, "аналог"
            )
        )
        drugsList.add(
            DrugsList(
                "мазь1", "мазь", true, "описание",
                "действующие вещества", "состав",
                "показания", "противопоказания",
                "побочные эффекты", "передозировка",
                "условия хранения", true, 150.0, "аналог"
            )
        )
        drugsList.add(
            DrugsList(
                "мазь11", "мазь", true, "описание",
                "действующие вещества", "состав",
                "показания", "противопоказания",
                "побочные эффекты", "передозировка",
                "условия хранения", true, 150.0, "аналог"
            )
        )
        drugsList.add(
            DrugsList(
                "мазь2", "мазь", true, "описание",
                "действующие вещества", "состав",
                "показания", "противопоказания",
                "побочные эффекты", "передозировка",
                "условия хранения", true, 150.0, "аналог"
            )
        )
        drugsList.add(
            DrugsList(
                "мазь3", "мазь", true, "описание",
                "действующие вещества", "состав",
                "показания", "противопоказания",
                "побочные эффекты", "передозировка",
                "условия хранения", true, 150.0, "аналог"
            )
        )
        drugsList.add(
            DrugsList(
                "укол1", "укол", true, "описание",
                "действующие вещества", "состав",
                "показания", "противопоказания",
                "побочные эффекты", "передозировка",
                "условия хранения", true, 150.0, "аналог"
            )
        )
        drugsList.add(
            DrugsList(
                "укол11", "укол", true, "описание",
                "действующие вещества", "состав",
                "показания", "противопоказания",
                "побочные эффекты", "передозировка",
                "условия хранения", true, 150.0, "аналог"
            )
        )
        drugsList.add(
            DrugsList(
                "укол2", "укол", true, "описание",
                "действующие вещества", "состав",
                "показания", "противопоказания",
                "побочные эффекты", "передозировка",
                "условия хранения", true, 150.0, "аналог"
            )
        )
        drugsList.add(
            DrugsList(
                "укол3", "укол", true, "описание",
                "действующие вещества", "состав",
                "показания", "противопоказания",
                "побочные эффекты", "передозировка",
                "условия хранения", true, 150.0, "аналог"
            )
        )
    }


    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = filteredDrugsList.filter {
                it.name.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
            }

            if (filteredList.isEmpty()) {
                binding.textBlank.visibility = View.VISIBLE
                binding.recycler.visibility = View.GONE
            } else {
                binding.textBlank.visibility = View.GONE
                binding.recycler.visibility = View.VISIBLE
                adapter.setFilteredList(filteredList)
            }
        }

    }
//    private fun addDataToListPill() {
//        drugsList.add(
//            DrugsList("таблетка1","таблетки", true,"описание",
//                "действующие вещества", "состав",
//                "показания","противопоказания",
//                "побочные эффекты","передозировка",
//                "условия хранения",true, 150.0, "аналог")
//        )
//        drugsList.add(
//            DrugsList("таблетка11","таблетки", true,"описание",
//                "действующие вещества", "состав",
//                "показания","противопоказания",
//                "побочные эффекты","передозировка",
//                "условия хранения",true, 150.0, "аналог")
//        )
//        drugsList.add(
//            DrugsList("таблетка2","таблетки", true,"описание",
//                "действующие вещества", "состав",
//                "показания","противопоказания",
//                "побочные эффекты","передозировка",
//                "условия хранения",true, 150.0, "аналог")
//        )
//        drugsList.add(
//            DrugsList("таблетка3","таблетки", true,"описание",
//                "действующие вещества", "состав",
//                "показания","противопоказания",
//                "побочные эффекты","передозировка",
//                "условия хранения",true, 150.0, "аналог")
//        )
//    }
//
//    private fun addDataToListSpray() {
//        drugsList.add(
//            DrugsList("спрей1","таблетки", true,"описание",
//                "действующие вещества", "состав",
//                "показания","противопоказания",
//                "побочные эффекты","передозировка",
//                "условия хранения",true, 150.0, "аналог")
//        )
//        drugsList.add(
//            DrugsList("спрей11","таблетки", true,"описание",
//                "действующие вещества", "состав",
//                "показания","противопоказания",
//                "побочные эффекты","передозировка",
//                "условия хранения",true, 150.0, "аналог")
//        )
//        drugsList.add(
//            DrugsList("спрей2","таблетки", true,"описание",
//                "действующие вещества", "состав",
//                "показания","противопоказания",
//                "побочные эффекты","передозировка",
//                "условия хранения",true, 150.0, "аналог")
//        )
//        drugsList.add(
//            DrugsList("спрей3","таблетки", true,"описание",
//                "действующие вещества", "состав",
//                "показания","противопоказания",
//                "побочные эффекты","передозировка",
//                "условия хранения",true, 150.0, "аналог")
//        )
//    }
//
//    private fun addDataToListCream() {
//        drugsList.add(
//            DrugsList("мазь1","таблетки", true,"описание",
//                "действующие вещества", "состав",
//                "показания","противопоказания",
//                "побочные эффекты","передозировка",
//                "условия хранения",true, 150.0, "аналог")
//        )
//        drugsList.add(
//            DrugsList("мазь11","таблетки", true,"описание",
//                "действующие вещества", "состав",
//                "показания","противопоказания",
//                "побочные эффекты","передозировка",
//                "условия хранения",true, 150.0, "аналог")
//        )
//        drugsList.add(
//            DrugsList("мазь2","таблетки", true,"описание",
//                "действующие вещества", "состав",
//                "показания","противопоказания",
//                "побочные эффекты","передозировка",
//                "условия хранения",true, 150.0, "аналог")
//        )
//        drugsList.add(
//            DrugsList("мазь3","таблетки", true,"описание",
//                "действующие вещества", "состав",
//                "показания","противопоказания",
//                "побочные эффекты","передозировка",
//                "условия хранения",true, 150.0, "аналог")
//        )
//    }
//
//    private fun addDataToListOintment() {
//        drugsList.add(
//            DrugsList("укол1","таблетки", true,"описание",
//                "действующие вещества", "состав",
//                "показания","противопоказания",
//                "побочные эффекты","передозировка",
//                "условия хранения",true, 150.0, "аналог")
//        )
//        drugsList.add(
//            DrugsList("укол11","таблетки", true,"описание",
//                "действующие вещества", "состав",
//                "показания","противопоказания",
//                "побочные эффекты","передозировка",
//                "условия хранения",true, 150.0, "аналог")
//        )
//        drugsList.add(
//            DrugsList("укол2","таблетки", true,"описание",
//                "действующие вещества", "состав",
//                "показания","противопоказания",
//                "побочные эффекты","передозировка",
//                "условия хранения",true, 150.0, "аналог")
//        )
//        drugsList.add(
//            DrugsList("укол3","таблетки", true,"описание",
//                "действующие вещества", "состав",
//                "показания","противопоказания",
//                "побочные эффекты","передозировка",
//                "условия хранения",true, 150.0, "аналог")
//        )
//    }
}
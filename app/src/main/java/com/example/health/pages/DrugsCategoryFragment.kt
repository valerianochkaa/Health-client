package com.example.health.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.health.R
import com.example.health.databinding.FragmentDrugsCategoryBinding

class DrugsCategoryFragment : Fragment(R.layout.fragment_drugs_category) {
    private var _binding: FragmentDrugsCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDrugsCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardPill.setOnClickListener{
            val category = Bundle()
            category.putString("categoryName", "Таблетки")
            findNavController().navigate(R.id.drugs_category_to_drugs, category)
        }
        binding.cardSpray.setOnClickListener{
            val category = Bundle()
            category.putString("categoryName", "Спреи")
            findNavController().navigate(R.id.drugs_category_to_drugs, category)
        }
        binding.cardCream.setOnClickListener{
            val category = Bundle()
            category.putString("categoryName", "Мази")
            findNavController().navigate(R.id.drugs_category_to_drugs, category)
        }
        binding.cardInjection.setOnClickListener{
            val category = Bundle()
            category.putString("categoryName", "Уколы")
            findNavController().navigate(R.id.drugs_category_to_drugs, category)
        }
    }
}
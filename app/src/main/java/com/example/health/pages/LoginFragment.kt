package com.example.health.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.health.MainActivity
import com.example.health.R
import com.example.health.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {
    // View binding
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigation Component
        val navigationMap = mapOf(
            binding.btnLogin to R.id.login_to_drugs_category,
            binding.btnReg to R.id.login_to_registration,
            binding.btnNoLogin to R.id.login_to_drugs_category,
        )
        navigationMap.forEach { (view, destination) ->
            view.setOnClickListener {
                findNavController().navigate(destination)
                if (destination == R.id.login_to_drugs_category) {
                    (activity as MainActivity).setActiveMenuItem(R.id.drugsCategory)
                }
            }
        }
    }
}


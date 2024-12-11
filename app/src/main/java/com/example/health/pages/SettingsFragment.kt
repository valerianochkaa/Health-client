package com.example.health.pages

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.example.health.R
import com.example.health.databinding.FragmentSettingsBinding
import com.example.health.utils.ThemeUtils

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("current_theme", false)
        binding.switchTheme.isChecked = isDarkMode
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            ThemeUtils.saveTheme(requireContext(), isChecked)
            ThemeUtils.applyTheme(requireContext())
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }


        binding.btnLogout.setOnClickListener {
            logoutUser()
        }

        binding.btnDialog.setOnClickListener {
            DialogAboutApp(
                onSubmitClickListener = { quantity ->
                    Toast.makeText(requireContext(), "DialogAboutApp", Toast.LENGTH_SHORT).show()
                }
            ).show(parentFragmentManager, "dialog")
        }
    }

    private fun logoutUser() {
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("token")
            apply()
        }
        findNavController().navigate(R.id.settings_to_login)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

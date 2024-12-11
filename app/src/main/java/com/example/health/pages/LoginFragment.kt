package com.example.health.pages

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.example.health.MainActivity
import com.example.health.R
import com.example.health.data.users.LoginResponse
import com.example.health.data.users.UserCredentials
import com.example.health.databinding.FragmentLoginBinding
import com.example.health.utils.RetrofitClient
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var token: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isUserLoggedIn()) {
            navigateToDrugsCategory()
            return
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.editPass.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                hideKeyboard()
                loginUser(email, password)
            } else {
                hideKeyboard()
                Snackbar.make(binding.root, "Пожалуйста, заполните все поля", Snackbar.LENGTH_SHORT).show()
            }
        }
        setupNavigation()
    }

    private fun setupNavigation() {
        val navigationMap = mapOf(
            binding.btnReg to R.id.login_to_registration,
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

    private fun loginUser(email: String, password: String) {
        val userCredentials = UserCredentials(email, password)
        RetrofitClient.userApi.loginUser(userCredentials).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("LoginFragment", "Response received: ${response.code()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        token = it.token
                        Log.d("LoginFragment", "Authorization successful! Token: $token")
                        saveToken(it.token, email)
                        navigateToDrugsCategory()
                    }
                } else {
                    Log.e("LoginFragment", "Authorization error: ${response.message()}")
                    Snackbar.make(binding.root, "Ошибка авторизации. Проверьте данные.", Snackbar.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginFragment", "Network error: ${t.message}")
                Snackbar.make(binding.root, "Ошибка сети. Попробуйте позже.", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveToken(token: String, email: String) {
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("token", token)
            putString("tokenEmail", email)
            apply()
        }
        Log.d("LoginFragment", "Token saved to SharedPreferences")
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", null)
        return !token.isNullOrEmpty()
    }

    private fun navigateToDrugsCategory() {
        findNavController().navigate(R.id.login_to_drugs_category)
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity?.currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



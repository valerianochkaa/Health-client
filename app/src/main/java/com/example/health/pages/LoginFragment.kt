package com.example.health.pages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.health.MainActivity
import com.example.health.R
import com.example.health.data.users.LoginResponse
import com.example.health.data.users.UserCredentials
import com.example.health.databinding.FragmentLoginBinding
import com.example.health.utils.RetrofitInstance
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

        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.editPass.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Snackbar.make(binding.root, "Пожалуйста, заполните все поля", Snackbar.LENGTH_SHORT).show()
            }
        }
        setupNavigation()
    }

    private fun setupNavigation() {
        val navigationMap = mapOf(
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

    private fun loginUser(email: String, password: String) {
        val userCredentials = UserCredentials(email, password)
        RetrofitInstance.userApi.loginUser(userCredentials).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("LoginFragment", "Response received: ${response.code()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        token = it.token
                        Log.d("LoginFragment","Authorization successful! Token: $token")
                        findNavController().navigate(R.id.login_to_drugs_category)
                    }
                } else {
                    Log.e("LoginFragment","Authorization error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginFragment", "Network error: ${t.message}")
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


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
import com.example.health.R
import com.example.health.data.users.LoginResponse
import com.example.health.data.users.UserCredentials
import com.example.health.databinding.FragmentRegistrationBinding
import com.example.health.utils.RetrofitClient
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnReg.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPass.text.toString().trim()
            val confirmPassword = binding.editReturnPass.text.toString().trim()

            when {
                email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                    showSnackbar("Пожалуйста, заполните все поля")
                    hideKeyboard()
                }
                password != confirmPassword -> {
                    showSnackbar("Пароли не совпадают")
                    hideKeyboard()
                }
                else -> {
                    hideKeyboard()
                    registerUser(email, password)
                }
            }
        }

        setupNavigation()
    }

    private fun setupNavigation() {
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.registration_to_login)
        }
    }

    private fun registerUser(email: String, password: String) {
        val userCredentials = UserCredentials(email, password)

        RetrofitClient.userApi.registerUser(userCredentials).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                when {
                    response.isSuccessful -> {
                        Log.d("RegistrationFragment", "Response received: ${response.code()}")
                        // Извлекаем токен из ответа
                        val token = response.body()?.token // Предполагается, что LoginResponse имеет поле token
                        if (token != null) {
                            Log.d("RegistrationFragment", "Registration successful! Token: $token")
                        } else {
                            Log.e("RegistrationFragment", "Token is null")
                        }
                        // Здесь можно обработать успешный ответ, например, сохранить токен
                        findNavController().navigate(R.id.registration_to_drugs_category)
                    }
                    response.code() == 400 -> {
                        showSnackbar("Некорректные данные. Проверьте введенные данные.")
                    }
                    response.code() == 409 -> {
                        showSnackbar("Пользователь с таким email уже существует.")
                    }
                    else -> {
                        Log.e("RegistrationFragment", "Registration error: ${response.message()}")
                        showSnackbar("Ошибка регистрации. Попробуйте еще раз.")
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("RegistrationFragment", "Network error: ${t.message}")
                showSnackbar("Ошибка сети. Проверьте подключение к интернету.")
            }
        })
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
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


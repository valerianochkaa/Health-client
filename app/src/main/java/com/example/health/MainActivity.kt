package com.example.health

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.database.tokens.TokenDTO
import com.example.health.utils.RetrofitClient
import com.example.health.utils.ThemeUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.applyTheme(this)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.navHostFragment)
        navView = findViewById(R.id.bottomNavigation)
        checkTokenAndNavigate()
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            navView.visibility = if (destination.id in listOf(
                    R.id.drugsCategoryFragment,
                    R.id.likesFragment,
                    R.id.diaryFragment,
                    R.id.settingsFragment
                )) View.VISIBLE else View.GONE
        }
        navView.setOnNavigationItemSelectedListener { item ->
            val destinationId = when (item.itemId) {
                R.id.drugsCategory -> R.id.drugsCategoryFragment
                R.id.likesFragment -> R.id.likesFragment
                R.id.diaryFragment -> R.id.diaryFragment
                R.id.settingsFragment -> R.id.settingsFragment
                else -> return@setOnNavigationItemSelectedListener false
            }
            if (navController.currentDestination?.id != destinationId) {
                navController.navigate(destinationId)
            }
            true
        }
        if (savedInstanceState == null) {
            navController.navigate(R.id.drugsCategoryFragment)
        }
    }


    private fun checkTokenAndNavigate() {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val tokenId = sharedPreferences.getString("tokenId", null)
        val tokenEmail = sharedPreferences.getString("tokenEmail", null)
        val token = sharedPreferences.getString("token", null)

        if (tokenId != null && tokenEmail != null && token != null) {
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.tokenApi.checkToken(
                        TokenDTO(tokenId, tokenEmail, token)
                    )
                    if (response.valid) {
                        navController.navigate(R.id.drugsCategoryFragment)
                    } else {
                        navController.navigate(R.id.loginFragment)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    navController.navigate(R.id.loginFragment)
                }
            }
        } else {
            navController.navigate(R.id.loginFragment)
        }
    }

    fun setActiveMenuItem(itemId: Int) {
        navView.selectedItemId = itemId
    }
}




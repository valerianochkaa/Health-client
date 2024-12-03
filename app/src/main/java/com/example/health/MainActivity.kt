package com.example.health

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.health.utils.ThemeUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    // Navigation Component
    private lateinit var navController: NavController
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        // Dark Theme
        ThemeUtils.applyTheme(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        navController = findNavController(R.id.navHostFragment)
        navView = findViewById(R.id.bottomNavigation)

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
            navController.navigate(destinationId)
            true
        }
    }

    fun setActiveMenuItem(itemId: Int) {
        navView.selectedItemId = itemId
    }
}

package eu.hanna.foodapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import eu.hanna.foodapp.R
import eu.hanna.foodapp.db.MealDatabase
import eu.hanna.foodapp.viewModel.HomeViewModel
import eu.hanna.foodapp.viewModel.HomeViewModelFactory

class MainActivity : AppCompatActivity() {

    // It can access the viewmodel from every fragments
    val viewModel: HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelProviderFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this,homeViewModelProviderFactory)[HomeViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inflate the bottom navigation and set up the bottom navigation with navigation controller
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = Navigation.findNavController(this, R.id.frag_host)
        NavigationUI.setupWithNavController(bottomNavigation,navController)

        // println
        
    }
}
package com.example.projectapp

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.core.view.MenuItemCompat.setOnActionExpandListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.projectapp.databinding.ActivityMasterBinding
import com.example.projectapp.utils.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

val sharedViewModel: SharedViewModel = SharedViewModel()

class MasterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMasterBinding
    private lateinit var mAppBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_master)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        mAppBarConfiguration = AppBarConfiguration(
                setOf(R.id.nav_profile_menu, R.id.nav_cars_menu, R.id.nav_recommended_menu))

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        val bottomNav = setupBottomNavMenu(navController)
        setupActionBar(navController, mAppBarConfiguration)

        binding.sharedViewModel = sharedViewModel
        //Show the BottomNavigationView
        sharedViewModel.setBottomNavigationViewVisibility(isVisible = false)
        //Show the Toolbar
        sharedViewModel.setActiveIntroStarted(false)
        //To control the BottomNavigationView visibility.
        sharedViewModel.onBottomNavigationViewVisibilitySelected.observe(this, Observer { isVisible ->
            if (isVisible) {
                binding.bottomNavView.visibility = View.VISIBLE
            } else {
                binding.bottomNavView.visibility = View.GONE
            }
        })

        bottomNav.setOnNavigationItemReselectedListener {
            Snackbar.make(binding.root, "Currently selected", Snackbar.LENGTH_SHORT).show();
        }
        //To control the Toolbar visibility.
        sharedViewModel.onStartIntroLayout.observe(this, Observer { isVisible ->
            if (isVisible) {
                toolbar.visibility = View.GONE
            } else {
                toolbar.visibility = View.VISIBLE
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return this.findNavController(R.id.nav_host_fragment).navigateUp(mAppBarConfiguration)
    }

    private fun setupBottomNavMenu(navController: NavController): BottomNavigationView {
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavView?.setupWithNavController(navController)
        return bottomNavView
    }

    private fun setupActionBar(navController: NavController,
                               appBarConfig: AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarConfig)
    }
}

package com.example.projectapp

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import kotlinx.android.synthetic.main.activity_master.*

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

        setupBottomNavMenu(navController)
        setupActionBar(navController, mAppBarConfiguration)

        //window.decorView.systemUiVisibility=  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        // View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        binding.sharedViewModel = sharedViewModel
        sharedViewModel.setBottomNavigationViewVisibility(false)

        sharedViewModel.onBottomNavigationViewVisibilitySelected.observe(this, Observer {
            if (it) {
                binding.bottomNavView.visibility = View.VISIBLE
            } else {
                binding.bottomNavView.visibility = View.GONE
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.top_app_bar_menu, menu)
        return true
    }


    override fun onSupportNavigateUp(): Boolean {
        return this.findNavController(R.id.nav_host_fragment).navigateUp(mAppBarConfiguration)
    }


    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavView?.setupWithNavController(navController)
    }

    private fun setupActionBar(navController: NavController,
                               appBarConfig: AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarConfig)
    }
}

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

        //window.decorView.systemUiVisibility=  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        // View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        binding.sharedViewModel = sharedViewModel
        sharedViewModel.setBottomNavigationViewVisibility(false)
        sharedViewModel.setActiveIntroStarted(false)

        sharedViewModel.onBottomNavigationViewVisibilitySelected.observe(this, Observer {
            if (it) {
                binding.bottomNavView.visibility = View.VISIBLE
            } else {
                binding.bottomNavView.visibility = View.GONE
            }
        })

        bottomNav.setOnNavigationItemReselectedListener {
            Snackbar.make(binding.root, "The tab is currently selected", Snackbar.LENGTH_SHORT).show();
        }


        sharedViewModel.onStartIntroLayout.observe(this, Observer {
            if (it) {
                toolbar.visibility = View.GONE
            } else {
                toolbar.visibility = View.VISIBLE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        /*
         * The call to getSearchableInfo() obtains a SearchableInfo object that is created from the searchable configuration XML file.
         * When the searchable configuration is correctly associated with your SearchView,
         * the SearchView starts an activity with the ACTION_SEARCH intent when a user submits a query.
         * You now need an activity that can filter for this intent and handle the search query.
         */

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.user_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        /**
         * A SearchView tries to start an activity with the ACTION_SEARCH when a user submits a search query.
         * A searchable activity filters for the ACTION_SEARCH intent and searches for the query in some sort of data set.
         * To create a searchable activity, declare an activity of your choice to filter for the ACTION_SEARCH intent:
         */

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onSearchRequested()

        return true
    }

    override fun onSearchRequested(): Boolean {
        //val jargon: Boolean = intent.getBundleExtra(SearchManager.APP_DATA)?.getBoolean(JARGON) ?: false

        val appData = Bundle().apply {
            putBoolean("JARGON", true)
        }
        startSearch(null, false, appData, false)
        return true
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

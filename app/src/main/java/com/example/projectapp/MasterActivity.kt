package com.example.projectapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.projectapp.databinding.ActivityMasterBinding

class MasterActivity : AppCompatActivity() {
    private var binding: ActivityMasterBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_master)
        val controller = Navigation.findNavController(this, R.id.get_started_nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, controller)
    }

    override fun onSupportNavigateUp(): Boolean {
        val controller = Navigation.findNavController(this, R.id.get_started_nav_host_fragment)
        return controller.navigateUp()
    }
}
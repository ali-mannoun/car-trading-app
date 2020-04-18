package com.example.projectapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.example.projectapp.databinding.ActivityMasterBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_cars.view.*


class MasterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMasterBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_master)

        //window.decorView.systemUiVisibility=  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        // View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN


        val controller = Navigation.findNavController(this, R.id.get_started_nav_host_fragment)

        //NavigationUI.setupActionBarWithNavController(this, controller)








    }

    /*
    override fun onSupportNavigateUp(): Boolean {
        val controller = Navigation.findNavController(this, R.id.get_started_nav_host_fragment)
        return controller.navigateUp()
    }
    */
}
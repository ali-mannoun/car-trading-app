package com.example.projectapp.ui.account

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.projectapp.IntroViewPagerAdapter
import com.example.projectapp.R
import com.example.projectapp.ScreenItem
import com.example.projectapp.databinding.FragmentGetStartedBinding
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_get_started.view.*

class GetStartedFragment : Fragment() {
    private lateinit var binding: FragmentGetStartedBinding
    private val viewModel: GetStartedViewModel by viewModels()

    private lateinit var screenPager: ViewPager2
    var introViewPagerAdapter: IntroViewPagerAdapter? = null
    lateinit var tabIndicator: TabLayout
    var btnNext: Button? = null
    var position = 0
    var btnGetStarted: Button? = null
    var btnAnim: Animation? = null
    var tvSkip: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_started, container, false)

        var pref: SharedPreferences = requireContext().getSharedPreferences("myPrefs", MODE_PRIVATE)
        val isIntroOpenedBefore :Boolean = pref.getBoolean("isIntroOpened",false)
        if(isIntroOpenedBefore){
            //todo remove from the back stack so when we enter the login screen we can press the back button to go back the launcher
            this.findNavController().navigate(GetStartedFragmentDirections.actionGetStartedFragmentToLoginFragment())
        }


        binding.getStartedBtn.setOnClickListener { view: View? ->
            view?.findNavController()?.navigate(GetStartedFragmentDirections.actionGetStartedFragmentToLoginFragment())
            var sp: SharedPreferences = requireContext().getSharedPreferences("myPrefs", MODE_PRIVATE)
            var editor: SharedPreferences.Editor = sp.edit()
            editor.putBoolean("isIntroOpened", true)
            editor.apply()
        }

        tabIndicator = binding.tabLayout

        // fill list screen
        val mList: MutableList<ScreenItem> = ArrayList()
        mList.add(ScreenItem("Fresh Food", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit",
                R.drawable.side_nav_bar))
        mList.add(ScreenItem("Fast Delivery", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit",
                R.drawable.side_nav_bar))
        mList.add(ScreenItem("Easy Payment", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit",
                R.drawable.side_nav_bar))


        // setup viewpager
        screenPager = binding.viewpager
        introViewPagerAdapter = IntroViewPagerAdapter(context, mList)
        screenPager.adapter = introViewPagerAdapter


        TabLayoutMediator(tabIndicator, screenPager) { tab, position ->
        }.attach()

        binding.nextBtn.setOnClickListener {
            var position = screenPager.currentItem
            if (position < mList.size) {
                position++
                screenPager.setCurrentItem(position)
            }
            if (position == mList.size) {
                //we reach the last screen
                binding.nextBtn.visibility = View.INVISIBLE
                binding.tabLayout.visibility = View.INVISIBLE
                binding.getStartedBtn.visibility = View.VISIBLE
            }
        }

        return binding.root
    }
}
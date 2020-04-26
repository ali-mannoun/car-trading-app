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
import androidx.viewpager2.widget.ViewPager2
import com.example.projectapp.R
import com.example.projectapp.databinding.FragmentGetStartedBinding
import com.example.projectapp.sharedViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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

        sharedViewModel.setActiveIntroStarted(true)


        val pref: SharedPreferences = requireContext().getSharedPreferences("myPrefs", MODE_PRIVATE)
        val isIntroOpenedBefore: Boolean = pref.getBoolean("isIntroOpened", false)
        if (isIntroOpenedBefore) {
            this.findNavController().graph.startDestination = R.id.nav_cars_menu
            this.findNavController().navigate(GetStartedFragmentDirections.actionGetStartedFragmentToNavCarsMenu())
        } else {
            this.findNavController().graph.startDestination = R.id.getStartedFragment
        }

        binding.getStartedBtn.setOnClickListener { view: View? ->
            view?.findNavController()?.navigate(GetStartedFragmentDirections.actionGetStartedFragmentToNavCarsMenu())
            val sp: SharedPreferences = requireContext().getSharedPreferences("myPrefs", MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sp.edit()
            editor.putBoolean("isIntroOpened", true)
            editor.apply()
        }

        tabIndicator = binding.tabLayout

        // fill list screen
        val mList: MutableList<ScreenItem> = ArrayList()
        mList.add(ScreenItem("Browse Cars", "Start browsing the cars provided by companies , and add your favourite ones to your list.",
                R.drawable.car_intro))
        mList.add(ScreenItem("Recommendation System", "Based on your search and app usage , we provide a recommendation engine that shows the top-related cars to you .",
                R.drawable.recommendation_intro))

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
                screenPager.currentItem = position
            }
            if (position == mList.size) {
                //we reach the last screen
                binding.nextBtn.visibility = View.INVISIBLE
                binding.tabLayout.visibility = View.INVISIBLE
                binding.getStartedBtn.visibility = View.VISIBLE
            }
        }
        /*
//todo solve the problem when we press the circles
        var position = screenPager.currentItem
        if (position == mList.size - 1) {
            //we reach the last screen
            binding.nextBtn.visibility = View.INVISIBLE
            binding.tabLayout.visibility = View.INVISIBLE
            binding.getStartedBtn.visibility = View.VISIBLE
        }
*/
        return binding.root
    }

}
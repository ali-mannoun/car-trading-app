package com.example.projectapp.ui.account

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.projectapp.R
import com.example.projectapp.databinding.FragmentGetStartedBinding
import com.example.projectapp.sharedViewModel
import com.example.projectapp.utils.IS_INTRO_SCREEN_OPENED_BEFORE
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class GetStartedFragment : Fragment() {

    private lateinit var binding: FragmentGetStartedBinding
    private lateinit var screenPager: ViewPager2
    private lateinit var introViewPagerAdapter: IntroViewPagerAdapter
    private lateinit var tabIndicator: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_started, container, false)
        //To hide the toolbar in the [MasterActivity] when we show this fragment.
        sharedViewModel.setActiveIntroStarted(true)
        //To show this fragment only once when the user open the app for the first time .
        val pref: SharedPreferences = requireContext().getSharedPreferences("myPrefs", MODE_PRIVATE)
        val isIntroOpenedBefore: Boolean = pref.getBoolean(IS_INTRO_SCREEN_OPENED_BEFORE, false)
        if (isIntroOpenedBefore) {
            //if opened, then set the first fragment is CarFragment.
            findNavController().graph.startDestination = R.id.nav_cars_menu
            findNavController().navigate(GetStartedFragmentDirections.actionGetStartedFragmentToNavCarsMenu())
        } else {
            //else set the first fragment is this fragment.
            this.findNavController().graph.startDestination = R.id.getStartedFragment
        }

        tabIndicator = binding.tabLayout
        // fill list screen
        val mList: MutableList<ScreenItem> = ArrayList()
        mList.add(ScreenItem("Browse Cars", "Start browsing the cars provided by companies , and add your favourite ones to your list.",
                R.drawable.ic_cars_intro))
        mList.add(ScreenItem("Recommendation System", "Based on your search and app usage , we provide a recommendation engine that shows the top-related cars to you .",
                R.drawable.ic_recommended_intro))
        mList.add(ScreenItem("Explore Latest Cars", "You will get notified when new cars have been added to the app",
                R.drawable.ic_notification_intro))
        // setup viewpager
        screenPager = binding.viewpager
        introViewPagerAdapter = IntroViewPagerAdapter(requireContext(), mList)
        screenPager.adapter = introViewPagerAdapter
        //To attach the ViewPager(screenPager) with TabLayout(tabIndicator)
        TabLayoutMediator(tabIndicator, screenPager) { tab, position ->
        }.attach()

        binding.getStartedBtn.setOnClickListener {
            //store this information to not show this fragment again.
            val sp: SharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sp.edit()
            editor.putBoolean(IS_INTRO_SCREEN_OPENED_BEFORE, true)
            editor.apply()

            findNavController().navigate(GetStartedFragmentDirections.actionGetStartedFragmentToNavCarsMenu())
        }
        return binding.root
    }
}

//items to show in the ViewPager.
class ScreenItem(val title: String, val description: String, val screenImg: Int)

//Change the superclass to RecyclerView.Adapter for paging through views, or FragmentStateAdapter for paging through fragments.
class IntroViewPagerAdapter(var mContext: Context, var mListScreen: List<ScreenItem>) :
        RecyclerView.Adapter<IntroViewPagerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.fragment_item_slider, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return mListScreen.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgSlide: ImageView
        var title: TextView
        var description: TextView

        init {
            imgSlide = itemView.findViewById(R.id.image_intro)
            title = itemView.findViewById(R.id.title)
            description = itemView.findViewById(R.id.description)
        }

        fun bind(position: Int) {
            title.text = mListScreen[position].title
            description.text = mListScreen[position].description
            imgSlide.setImageResource(mListScreen[position].screenImg)
        }
    }
}
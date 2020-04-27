package com.example.projectapp.ui.cars_specifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.projectapp.R
import com.example.projectapp.database.CarsDatabase
import com.example.projectapp.databinding.ActivityCarSpecificationsBinding
import com.example.projectapp.databinding.CarDetailsLayoutBinding
import com.example.projectapp.network.getNetworkService
import com.example.projectapp.repository.CarRepository
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CarSpecificationsActivity : AppCompatActivity() {
    private lateinit var MainLayoutBinding: ActivityCarSpecificationsBinding
    private lateinit var detailsLayoutBinding: CarDetailsLayoutBinding

    private val safeArgs: CarSpecificationsActivityArgs by navArgs()
    private val viewModel: CarSpecificationsViewModel by viewModels(
            factoryProducer = {
                CarSpecificationsViewModel.FACTORY(CarRepository(getNetworkService(),
                        CarsDatabase.getInstance(requireNotNull(this).application).carsDatabaseDao))
            })

    private lateinit var screenPager: ViewPager2
    var imagesSlideAdapter: CarImagesViewPagerAdapter? = null
    lateinit var tabIndicator: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainLayoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_car_specifications)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        MainLayoutBinding.lifecycleOwner = this
        // Giving the binding access to the CarsViewModel
        // binding.viewModel = viewModel


        val carId = safeArgs.carId

        viewModel.loadCarSpecificationsById(carId)

        // fill list screen
        val mList: MutableList<ImageItem> = ArrayList()
        //mList.add(ImageItem())
        //mList.add(ImageItem())

        viewModel.car.observe(this, Observer {
            mList.add(ImageItem(it.generalInformation.mainImageUrl))
            mList.add(ImageItem(it.generalInformation.mainImageUrl))
            mList.add(ImageItem(it.generalInformation.mainImageUrl))

            tabIndicator = MainLayoutBinding.tabLayout
            screenPager = MainLayoutBinding.viewpager
            imagesSlideAdapter = CarImagesViewPagerAdapter(this, mList)
            screenPager.adapter = imagesSlideAdapter

            // setup viewpager
            TabLayoutMediator(tabIndicator, screenPager) { tab, position ->
            }.attach()

            //binding.collapsingToolbarLayout.title = it.generalInformation.brand
        })











    }
}

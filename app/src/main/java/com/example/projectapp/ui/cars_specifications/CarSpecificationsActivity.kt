package com.example.projectapp.ui.cars_specifications

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.projectapp.R
import com.example.projectapp.database.CarsDatabase
import com.example.projectapp.databinding.ActivityCarSpecificationsBinding
import com.example.projectapp.domain.CarSpecifications
import com.example.projectapp.network.getNetworkService
import com.example.projectapp.repository.CarRepository
import com.example.projectapp.ui.account.register.USER_ID
import com.example.projectapp.ui.account.register.USER_NAME
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_login.*

class CarSpecificationsActivity : AppCompatActivity() {

    private lateinit var mainLayoutBinding: ActivityCarSpecificationsBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var imagesSlideAdapter: CarImagesViewPagerAdapter
    private lateinit var tabIndicator: TabLayout
    private var carId: Long = -1L //initial value
    private var isCarInFavouriteList = false //initial value

    private val safeArgs: CarSpecificationsActivityArgs by navArgs()
    private val viewModel: CarSpecificationsViewModel by viewModels(
            factoryProducer = {
                CarSpecificationsViewModel.FACTORY(CarRepository(getNetworkService(),
                        CarsDatabase.getInstance(requireNotNull(this).application).carsDatabaseDao))
            })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainLayoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_car_specifications)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        mainLayoutBinding.lifecycleOwner = this
        // Giving the binding access to the CarsViewModel
        // binding.viewModel = viewModel
        tabIndicator = mainLayoutBinding.tabLayout
        viewPager = mainLayoutBinding.viewpager
        carId = safeArgs.carId

        val pref: SharedPreferences = baseContext.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val userId: String = requireNotNull(pref.getString(USER_ID, "-1"))
        val userName: String = requireNotNull(pref.getString(USER_NAME, "-1"))

        viewModel.loadCarSpecificationsById(carId)
        viewModel.checkFavouriteStatus(userId, carId)

        viewModel.favouriteStatus.observe(this, Observer { isFavourite ->
            Log.e("1", "ali")
            isCarInFavouriteList = isFavourite
            toggleFavouriteFab(isFavourite)
        })

        val carImagesList: MutableList<ImageItem> = ArrayList()

        viewModel.carDetails.observe(this, Observer {
            carImagesList.add(ImageItem(it.generalInformation.mainImageUrl))
            carImagesList.add(ImageItem(it.generalInformation.mainImageUrl))
            carImagesList.add(ImageItem(it.generalInformation.mainImageUrl))

            imagesSlideAdapter = CarImagesViewPagerAdapter(this, carImagesList)
            viewPager.adapter = imagesSlideAdapter
            // setup viewpager
            TabLayoutMediator(tabIndicator, viewPager) { tab, position ->
            }.attach()

            bindCarDetailsIntoViews(it)
        })

        mainLayoutBinding.favouriteFab.setOnClickListener {
            if (isCarInFavouriteList) {
                isCarInFavouriteList = false
                //remove car from favourite list.
                viewModel.removeCarFromFavouriteList(userId, carId)
                toggleFavouriteFab(false)
            } else {
                isCarInFavouriteList = true
                //add car to favourite list.
                viewModel.addCarToFavouriteList(userId, carId)
                toggleFavouriteFab(true)
            }
        }
    }

    private fun toggleFavouriteFab(isFavourite: Boolean) {
        if (isFavourite) {
            mainLayoutBinding.favouriteFab.text = "UnSave"
            mainLayoutBinding.favouriteFab.icon = applicationContext.getDrawable(R.drawable.ic_favorite_fill_24)
        } else {
            mainLayoutBinding.favouriteFab.text = "Save"
            mainLayoutBinding.favouriteFab.icon = applicationContext.getDrawable(R.drawable.ic_favorite_border_black_24dp)
        }
    }

    private fun bindCarDetailsIntoViews(car: CarSpecifications) {
        //1. General information
        mainLayoutBinding.collapsingToolbarLayout.setExpandedTitleColor(Color.argb(0, 0, 0, 0))
        mainLayoutBinding.collapsingToolbarLayout.title = "${car.generalInformation.brand}, ${car.generalInformation.model}"
        mainLayoutBinding.generation.text = car.generalInformation.generation
        mainLayoutBinding.yearPuttingProduction.text = car.generalInformation.yearOfPuttingIntoProduction
        mainLayoutBinding.yearStoppingProduction.text = car.generalInformation.yearOfStoppingProduction
        mainLayoutBinding.description.text = car.generalInformation.description
        //2. Internal combustion Engine
        mainLayoutBinding.power.text = car.engine.power
        mainLayoutBinding.engineModel.text = car.engine.model
        mainLayoutBinding.engineSpeed.text = car.engine.maxSpeed
        mainLayoutBinding.oilCapacity.text = car.engine.oilCapacity
        mainLayoutBinding.fuelSystem.text = car.engine.fuelSystem
        //3. Performance
        mainLayoutBinding.speed.text = car.performance.maxSpeed
        mainLayoutBinding.acceleration.text = car.performance.acceleration100Kmh
        mainLayoutBinding.fuelConsumption.text = car.performance.fuelConsumption
        mainLayoutBinding.co2Emission.text = car.performance.co2Emissions
        //4. Body type
        mainLayoutBinding.seats.text = car.body.seats
        mainLayoutBinding.doors.text = car.body.doors
        mainLayoutBinding.length.text = car.body.length
        mainLayoutBinding.width.text = car.body.width
        mainLayoutBinding.height.text = car.body.height
        mainLayoutBinding.maxWeight.text = car.body.maxWeight
        mainLayoutBinding.bodyType.text = car.body.bodyType
        mainLayoutBinding.fuelTankVolume.text = car.body.fuelTankVolume
        //5. Others
        mainLayoutBinding.brakes.text = car.others.brakes
        mainLayoutBinding.numOfGears.text = car.others.numberOfGears
        mainLayoutBinding.tireSize.text = car.others.tireSize
        mainLayoutBinding.exteriorFeatures.text = car.others.exteriorFeatures
        mainLayoutBinding.interiorFeatures.text = car.others.interiorFeatures

    }

}

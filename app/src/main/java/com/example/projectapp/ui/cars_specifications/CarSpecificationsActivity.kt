package com.example.projectapp.ui.cars_specifications

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.projectapp.R
import com.example.projectapp.database.CarsDatabase
import com.example.projectapp.databinding.ActivityCarSpecificationsBinding
import com.example.projectapp.databinding.CarDetailsLayoutBinding
import com.example.projectapp.domain.CarSpecifications
import com.example.projectapp.network.getNetworkService
import com.example.projectapp.repository.CarRepository
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CarSpecificationsActivity : AppCompatActivity() {
    private lateinit var mainLayoutBinding: ActivityCarSpecificationsBinding
    //private lateinit var detailsLayoutBinding: CarDetailsLayoutBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var imagesSlideAdapter: CarImagesViewPagerAdapter
    private lateinit var tabIndicator: TabLayout
    private var carId: Long = -1L //initial value
/*
    //General Information
    private lateinit var brand: TextView
    private lateinit var model: TextView
    private lateinit var generation: TextView
    private lateinit var yearOfPuttingIntoProduction: TextView
    private lateinit var yearOfStoppingProduction: TextView
    private lateinit var description: TextView

    //2. Internal combustion Engine
    private lateinit var power: TextView
    private lateinit var engineModel: TextView
    private lateinit var maxEngineSpeed: TextView
    private lateinit var engineOilCapacity: TextView
    private lateinit var fuelSystem: TextView

    //3. Performance
    private lateinit var maxSpeed: TextView
    private lateinit var acceleration100Km_h: TextView
    private lateinit var fuelConsumption: TextView
    private lateinit var co2Emission: TextView

    //4. Body type
    private lateinit var seats: TextView
    private lateinit var doors: TextView
    private lateinit var length: TextView
    private lateinit var width: TextView
    private lateinit var height: TextView
    private lateinit var maxWeight: TextView
    private lateinit var bodyType: TextView
    private lateinit var fuelTankVolume: TextView

    //5. Others
    private lateinit var brakes: TextView
    private lateinit var numberOfGears: TextView
    private lateinit var gearType: TextView
    private lateinit var tireSize: TextView
    private lateinit var exteriorFeatures: TextView
    private lateinit var interiorFeatures: TextView
*/
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
/*
        brand = detailsLayoutBinding.brand
        model = detailsLayoutBinding.model
        generation = detailsLayoutBinding.generation
        yearOfPuttingIntoProduction = detailsLayoutBinding.yearPuttingProduction
        yearOfStoppingProduction = detailsLayoutBinding.yearStoppingProduction
        description = detailsLayoutBinding.description
        power = detailsLayoutBinding.power
        engineModel = detailsLayoutBinding.engineModel
        maxEngineSpeed = detailsLayoutBinding.engineSpeed
        engineOilCapacity = detailsLayoutBinding.oilCapacity
        fuelSystem = detailsLayoutBinding.fuelSystem
        maxSpeed = detailsLayoutBinding.speed
        acceleration100Km_h = detailsLayoutBinding.acceleration
        fuelConsumption = detailsLayoutBinding.fuelConsumption
        co2Emission = detailsLayoutBinding.co2Emission
        seats = detailsLayoutBinding.seats
        doors = detailsLayoutBinding.doors
        length = detailsLayoutBinding.length
        width = detailsLayoutBinding.width
        height = detailsLayoutBinding.height
        maxWeight = detailsLayoutBinding.maxWeight
        bodyType = detailsLayoutBinding.bodyType
        fuelTankVolume = detailsLayoutBinding.fuelTankSystem
        brakes = detailsLayoutBinding.brakes
        numberOfGears = detailsLayoutBinding.numOfGears
        gearType = detailsLayoutBinding.gearType
        tireSize = detailsLayoutBinding.tireSize
        exteriorFeatures = detailsLayoutBinding.exteriorFeatures
        interiorFeatures = detailsLayoutBinding.interiorFeatures
*/
        carId = safeArgs.carId
        viewModel.loadCarSpecificationsById(carId)

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
    }

    private fun bindCarDetailsIntoViews(car: CarSpecifications) {
        //1. General information
        mainLayoutBinding.collapsingToolbarLayout.setExpandedTitleColor(Color.argb(0,0,0,0))
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

package com.example.projectapp.ui.cars_specifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.navArgs
import com.example.projectapp.R
import com.example.projectapp.database.CarsDatabase
import com.example.projectapp.network.getNetworkService
import com.example.projectapp.repository.CarRepository

class CarSpecificationsActivity : AppCompatActivity() {
    //private lateinit var binding: FragmentCarDetailsBinding
    private val safeArgs: CarSpecificationsActivityArgs by navArgs()
    private val viewModel: CarSpecificationsViewModel by viewModels(
            factoryProducer = {
                CarSpecificationsViewModel.FACTORY(CarRepository(getNetworkService(),
                        CarsDatabase.getInstance(requireNotNull(this).application).carsDatabaseDao))
            })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_specifications)

        /*
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_car_details, container, false)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        // Giving the binding access to the CarsViewModel
        binding.viewModel = viewModel
        */
        val carId = safeArgs.carId


    }
}

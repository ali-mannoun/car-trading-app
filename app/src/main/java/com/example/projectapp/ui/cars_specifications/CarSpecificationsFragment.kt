package com.example.projectapp.ui.cars_specifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.projectapp.R
import com.example.projectapp.database.CarsDatabase
import com.example.projectapp.databinding.FragmentCarDetailsBinding
import com.example.projectapp.domain.CarSpecifications
import com.example.projectapp.network.getNetworkService
import com.example.projectapp.repository.CarRepository

class CarSpecificationsFragment : Fragment() {
    private lateinit var binding: FragmentCarDetailsBinding
    private val viewModel: CarSpecificationsViewModel by viewModels(
            factoryProducer = {
                CarSpecificationsViewModel.FACTORY(CarRepository(getNetworkService(),
                        CarsDatabase.getInstance(requireNotNull(this.activity).application).carsDatabaseDao))
            })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_car_details, container, false)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        // Giving the binding access to the CarsViewModel
        binding.viewModel = viewModel


        val carId = arguments?.get("carId").toString().toLong()

        viewModel.loadCarSpecificationsById(carId)

        viewModel.cars.observe(viewLifecycleOwner, Observer {
            it?.let { carSpecifications: CarSpecifications ->
                binding.result.text = carSpecifications.generalInformation.mainImageUrl
            }
        })

        return binding.root
    }
}
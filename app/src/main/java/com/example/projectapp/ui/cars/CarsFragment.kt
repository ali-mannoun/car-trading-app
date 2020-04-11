package com.example.projectapp.ui.cars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectapp.R
import com.example.projectapp.databinding.FragmentCarsBinding
import com.example.projectapp.network.getNetworkService
import com.example.projectapp.repository.CarRepository

class CarsFragment : Fragment() {
    private lateinit var binding: FragmentCarsBinding
    private val viewModel: CarsViewModel by viewModels(
            factoryProducer = {
                CarsViewModel.FACTORY(CarRepository(getNetworkService(),null))
            }
    )

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cars, container, false)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)
        // Giving the binding access to the CarsViewModel
        binding.viewModel = viewModel

        val adapter = CarsAdapter(CarsListener { carId ->
            Toast.makeText(context, "edit clicked : $carId", Toast.LENGTH_SHORT).show()
            viewModel.onCarClicked(carId)
            //      Bundle bundle = new Bundle();
            //       bundle.putParcelable("amount", car);
            //Navigation.findNavController(binding.getRoot()).navigate(CarsFragmentDirections.actionNavigationCarsUserToUserCarDetailsFragment())
        })

        val manager = GridLayoutManager(activity, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                //here we make the header only 2 spans wide , and the others have 1 span wide
                /*
                here we define how many span wide is every element occupied  , for example the header will take the 2 span wide ,
                and , the rest of elements will take one span wide .
                 */
                0 -> 2
                else -> 1
            }
        }
        binding.carsRecyclerView.layoutManager = manager
        binding.carsRecyclerView.adapter = adapter

        /*
        Your code needs to tell the ListAdapter when a changed list is available.
        ListAdapter provides a method called submitList() to tell ListAdapter that a new version of the list is available.
        When this method is called, the ListAdapter diffs the new list against the old one and detects items that were added,
        removed, moved, or changed. Then the ListAdapter updates the items shown by RecyclerView.
         */
        viewModel.cars.observe(viewLifecycleOwner, Observer {
            it?.let {
                //adapter.submitList(it)
            }
        })
       /*viewModel.navigateToCarDetails.observe(viewLifecycleOwner, Observer { carId ->
            carId.let {
                //TODO this.findNavController().navigate()...and pass carId as parameter
                viewModel.onCarDetailsNavigated()
            }
        })*/
        /*viewModel.data.observe(this, Observer {
            cars: List<Car?> -> adapter.submitList(cars)
        })*/

        return binding.root
    }
}
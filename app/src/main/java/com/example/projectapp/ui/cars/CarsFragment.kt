package com.example.projectapp.ui.cars

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.projectapp.R
import com.example.projectapp.database.CarsDatabase
import com.example.projectapp.databinding.FragmentCarsBinding
import com.example.projectapp.network.getNetworkService
import com.example.projectapp.repository.CarRepository
import com.example.projectapp.repository.UserRepository
import com.example.projectapp.sharedViewModel
import com.example.projectapp.ui.account.login.LoginViewModel
import com.example.projectapp.utils.CheckNetworkConnectivity

class CarsFragment : Fragment() {
    private lateinit var binding: FragmentCarsBinding
    private val loginViewModel: LoginViewModel by activityViewModels(
            factoryProducer = {
                LoginViewModel.FACTORY(UserRepository(getNetworkService()))
            }
    )
    private val carsViewModel: CarsViewModel by viewModels(
            factoryProducer = {
                CarsViewModel.FACTORY(CarRepository(getNetworkService(),
                        CarsDatabase.getInstance(requireNotNull(this.activity).application).carsDatabaseDao)
                )
            }
    )

    private fun showWelcomeMessage() {
        Toast.makeText(context, "welcome from car fragment", Toast.LENGTH_LONG).show()
        carsViewModel.refreshCars()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

        val pref: SharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val isRememberMeChecked: Boolean = pref.getBoolean("rememberMeChecked", false)
        if (isRememberMeChecked) {
            loginViewModel.rememberUser(true)
        }

        loginViewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> showWelcomeMessage()
                LoginViewModel.AuthenticationState.AUTHENTICATED_AND_REMEMBER_ME -> showWelcomeMessage()
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> findNavController().navigate(R.id.loginFragment)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        sharedViewModel.setBottomNavigationViewVisibility(true)
        sharedViewModel.setActiveIntroStarted(false)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cars, container, false)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        // Giving the binding access to the CarsViewModel
        binding.viewModel = carsViewModel


        val adapter = CarsAdapter(CarsListener { carId ->
            Toast.makeText(context, " clicked : $carId", Toast.LENGTH_SHORT).show()
            //viewModel.onCarClicked(carId)

            //      Bundle bundle = new Bundle();
            //       bundle.putParcelable("amount", car);
            //Navigation.findNavController(binding.getRoot()).navigate(CarsFragmentDirections.actionNavigationCarsUserToUserCarDetailsFragment())
        })

        //TODO when rotate the device show two grids . when the device isn't rotated show one grid.

        //val manager = GridLayoutManager(activity, 2)
        val manager = GridLayoutManager(activity, 1)
/*
//todo this when rotate device

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
        */
        binding.carsRecyclerView.layoutManager = manager
        binding.carsRecyclerView.adapter = adapter

        /*
        Your code needs to tell the ListAdapter when a changed list is available.
        ListAdapter provides a method called submitList() to tell ListAdapter that a new version of the list is available.
        When this method is called, the ListAdapter diffs the new list against the old one and detects items that were added,
        removed, moved, or changed. Then the ListAdapter updates the items shown by RecyclerView.
         */
        carsViewModel.cars.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.e("CarsFragment", "cars observed")
                adapter.addHeaderAndSubmitList(it)
            }
        })

        carsViewModel.navigateToSelectedCarDetails.observe(viewLifecycleOwner, Observer {
            it?.let { id: Long ->
                //load specifications
                this.findNavController().navigate(CarsFragmentDirections.actionNavCarsMenuToCarSpecificationsFragment(id))
                carsViewModel.onCarDetailsNavigated()
            }
        })

        binding.swipe.setOnRefreshListener {
            if (!CheckNetworkConnectivity.isOnline(requireNotNull(context))) {
                Toast.makeText(context, "No internet connection !", Toast.LENGTH_SHORT).show()
            } else {
                carsViewModel.refreshCars()
            }
            binding.swipe.isRefreshing = false

        }

        if (!CheckNetworkConnectivity.isOnline(requireNotNull(context))) {
            Toast.makeText(context, "Load data from offline cache !", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}
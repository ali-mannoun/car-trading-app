package com.example.projectapp.ui.cars

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
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
import com.example.projectapp.ui.account.observeAuthenticationStateInCarsFragment
import com.example.projectapp.utils.CheckNetworkConnectivity
import com.example.projectapp.utils.REMEMBER_ME
import com.example.projectapp.utils.USER_ID
import com.google.android.material.snackbar.Snackbar

class CarsFragment : Fragment() {
    private lateinit var binding: FragmentCarsBinding
    private lateinit var adapter: CarsAdapter
    private var isMainCarsListShown = true

    //You can share data between the fragments by having a ViewModel scoped to the activity, which implements ViewModelStoreOwner.
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

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val pref: SharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val isRememberMeChecked: Boolean = pref.getBoolean(REMEMBER_ME, false)
        if (isRememberMeChecked) {
            Log.e("remember me", isRememberMeChecked.toString())
            loginViewModel.rememberUser(true)
        }

        //show the BottomNavigationView and toolbar.
        sharedViewModel.setBottomNavigationViewVisibility(true)
        sharedViewModel.setActiveIntroStarted(true)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cars, container, false)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        // Giving the binding access to the CarsViewModel
        binding.viewModel = carsViewModel
        //If the user is authenticated then process.
        if (loginViewModel.authenticationState.value == LoginViewModel.AuthenticationState.AUTHENTICATED_AND_REMEMBER_ME ||
                loginViewModel.authenticationState.value == LoginViewModel.AuthenticationState.AUTHENTICATED) {

            adapter = CarsAdapter(CarsListener { carId ->
                //Click listener(when we click on car, we move to the carSpecification).
                carsViewModel.onCarClicked(carId)
            })

            val pref: SharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val userId: String = requireNotNull(pref.getString(USER_ID, "-1"))
            val manager = GridLayoutManager(activity, 1)

            binding.carsRecyclerView.layoutManager = manager
            binding.carsRecyclerView.adapter = adapter

//             TODO when rotate the device show two grids of cars, when the device isn't rotated show one grid of cars.
//            val manager = GridLayoutManager(activity, 2)
//            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//
//                override fun getSpanSize(position: Int) = when (position) {
//                    //here we make the header only 2 spans wide , and the others have 1 span wide
//                    /*
//                    here we define how many span wide is every element occupied  , for example the header will take the 2 span wide ,
//                     and , the rest of elements will take one span wide .
//                     */
//                    0 -> 2
//                    else -> 1
//                 }
//             }


            binding.swipe.setOnRefreshListener {
                binding.searchEditText.setText("")
                if (!CheckNetworkConnectivity.isOnline(requireNotNull(context))) {
                    Toast.makeText(context, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
                } else {
                    if (isMainCarsListShown) {
                        carsViewModel.refreshCars()
                    } else {
                        carsViewModel.fetchFavouriteCars(userId)
                    }
                }
                binding.swipe.isRefreshing = false
            }

            binding.favouriteListFab.setOnClickListener {
                if (isMainCarsListShown) {
                    carsViewModel.onFavouriteFabClicked(userId, favouriteCarList = true)
                    binding.favouriteListFab.setImageDrawable(context?.getDrawable(R.drawable.ic_favorite_border_black_24dp))
                    //We set the adapter = null because we need to set the header immediately.
                    binding.carsRecyclerView.adapter = null
                    adapter.setHeaderValue(getString(R.string.favourite_car_list))
                    binding.carsRecyclerView.adapter = adapter
                    isMainCarsListShown = false
                } else {
                    carsViewModel.onFavouriteFabClicked(userId, favouriteCarList = false)
                    binding.favouriteListFab.setImageDrawable(context?.getDrawable(R.drawable.ic_favorite_fill_24))
                    binding.carsRecyclerView.adapter = null
                    adapter.setHeaderValue(getString(R.string.main_cars_list))
                    binding.carsRecyclerView.adapter = adapter
                    isMainCarsListShown = true
                }
            }

            binding.searchEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    adapter.filter.filter(p0)
                }
            })
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

        loginViewModel.observeAuthenticationStateInCarsFragment(viewLifecycleOwner, loginViewModel.authenticationState, findNavController()) {
            carsViewModel.refreshCars()
        }

        //If the user is authenticated then process.
        if (loginViewModel.authenticationState.value == LoginViewModel.AuthenticationState.AUTHENTICATED_AND_REMEMBER_ME ||
                loginViewModel.authenticationState.value == LoginViewModel.AuthenticationState.AUTHENTICATED) {

            //Observe main cars
            carsViewModel.cars.observe(viewLifecycleOwner, Observer {
                Log.e("CarsFragment", "cars observed")

                /**
                 * Your code needs to tell the ListAdapter when a changed list is available.
                 * ListAdapter provides a method called submitList() to tell ListAdapter that a new version of the list is available.
                 * When this method is called, the ListAdapter diffs the new list against the old one and detects items that were added,
                 * removed, moved, or changed. Then the ListAdapter updates the items shown by RecyclerView.
                 */
                it?.let {
                    adapter.addHeaderAndSubmitList(it)
                    adapter.setListData(it)
                }
            })
            //Observe favourite cars
            carsViewModel.favouriteCars.observe(viewLifecycleOwner, Observer {
                Log.e("CarsFragment", "cars favourite observed")

                it?.let {
                    adapter.addHeaderAndSubmitList(it)
                    adapter.setListData(it)
                }
            })

            carsViewModel.navigateToSelectedCarDetails.observe(viewLifecycleOwner, Observer {
                it?.let { id: Long ->
                    //load specifications
                    this.findNavController().navigate(CarsFragmentDirections.actionNavCarsMenuToCarSpecificationsActivity(id))
                    carsViewModel.onCarDetailsNavigated()
                }
            })

            carsViewModel.toast.observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    Snackbar.make(binding.root, getString(R.string.loading_cached_cars), Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(binding.root, it.toString(), Snackbar.LENGTH_LONG).show()
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        //We override this method because when dislike a car , we need to refresh favourite car list to remove the disliked car.
        val pref: SharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val userId: String = requireNotNull(pref.getString(USER_ID, "-1"))
        if (!isMainCarsListShown)
            carsViewModel.fetchFavouriteCars(userId)
    }
}
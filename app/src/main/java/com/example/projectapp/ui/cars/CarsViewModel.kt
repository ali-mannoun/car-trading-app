package com.example.projectapp.ui.cars

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.projectapp.database.CarsDatabaseDao
import com.example.projectapp.domain.Car
import com.example.projectapp.domain.CarSpecifications
import com.example.projectapp.network.CarProperty
import com.example.projectapp.repository.CarRepository
import com.example.projectapp.utils.singleArgViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

enum class CarsApiStatus { LOADING, ERROR, DONE }

/**
 * CarsViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 *
 * @param repository the data source this ViewModel will fetch results from.
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 */
class CarsViewModel(private val carRepository: CarRepository) : ViewModel() {

    companion object {
        /**
         * Factory for creating [CarsViewModel]
         *
         * @param arg the repository to pass to [CarsViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::CarsViewModel)
    }

    init {
        refreshDataFromRepository()
    }


    val cars: LiveData<List<Car>> = carRepository.cars

    private val _status = MutableLiveData<CarsApiStatus>()
    val status: LiveData<CarsApiStatus>
        get() = _status

    private val _spinner = MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    private val _favouriteCars = MutableLiveData<List<Car>>()
    val favouriteCars: LiveData<List<Car>>
        get() = _favouriteCars

    //When we click on a specific car.
    private val _selectedCar = MutableLiveData<Car>()
    val selectedCar: LiveData<Car>
        get() = _selectedCar

    private val _navigateToSelectedCarDetails = MutableLiveData<Long>()
    val navigateToSelectedCarDetails: LiveData<Long>
        get() = _navigateToSelectedCarDetails

    //Load the main list of cars.
    private fun refreshDataFromRepository(userId: String = "-1") = launchDataLoad {
        carRepository.refreshCars(userId)
    }

    fun refreshCars(userId: String = "-1") {
        refreshDataFromRepository(userId)
    }

    fun fetchFavouriteCars(userId: String) {
        viewModelScope.launch {
            try {
                onStartDownloading()
                _favouriteCars.value = carRepository.fetchFavouriteCarsList(userId)
                onDoneDownloading()
            } catch (exception: IOException) {
                _toast.value = "Unable to connect to the server !"
                onErrorDownloading()
            } finally {
                onDoneDownloading()
            }
        }
    }

    /**
     * You should usually handle clicks in the ViewModel,
     * because the ViewModel has access to the data and logic
     * for determining what needs to happen in response to the click.
     */
    fun onCarClicked(id: Long) {
        _navigateToSelectedCarDetails.value = id
    }

    fun onFavouriteFabClicked(userId: String, favouriteCarList: Boolean) = launchDataLoad {
        //if we want to load main cars or favourite cars.
        if (favouriteCarList) {
            fetchFavouriteCars(userId)
        } else {
            carRepository.refreshCars()
        }
    }

    /**
     * Define the method to call after the app has finished navigating.
     * Call it onCarDetailsNavigated() and set its value to null.
     */
    fun onCarDetailsNavigated() {
        _navigateToSelectedCarDetails.value = null
    }

    /**
     * Helper function to call a data load function with a loading spinner, errors will trigger a
     * toast.
     * this function is a high order function, that use the coroutines.
     * You don't often have to declare your own suspend lambdas,
     * but they can be helpful to create abstractions like this that encapsulate repeated logic!
     *
     * we can create a general data loading coroutine that always shows the spinner.
     * This might be helpful in a codebase that loads data in response to several events,
     * and wants to ensure the loading spinner is consistently displayed.
     *
     * By marking `block` as `suspend` this creates a suspend lambda which can call suspend
     * functions.
     * https://codelabs.developers.google.com/codelabs/kotlin-coroutines/index.html?index=..%2F..index#10
     *
     * @param block lambda to actually load data. It is called in the viewModelScope. Before calling the
     *              lambda the loading spinner will display, after completion or error the loading
     *              spinner will stop
     */
    private fun launchDataLoad(block: suspend () -> Unit) {
        /*
        The library adds a viewModelScope as an extension function of the ViewModel class.
        This scope is bound to Dispatchers.Main and will automatically be cancelled when the ViewModel is cleared.
         */
        viewModelScope.launch {
            try {
                onStartDownloading()
                //here we don't need to assing _car = block() because we are observing the cars from repository.
                block()
                onDoneDownloading()
            } catch (error: IOException) {
                if (cars.value.isNullOrEmpty()) {
                    _toast.value = "Unable to connect to the server !"
                    Log.e("CarsViewModel error", "cars.value.isNullOrEmpty()" + " ,,,," + error.message.toString())
                }
                _toast.value = "Unable to connect to the server !"
                Log.e("car vm", error.message.toString())
                onErrorDownloading()
            } finally {
                onDoneDownloading()
            }
        }
    }

    private fun onDoneDownloading() {
        _status.value = CarsApiStatus.DONE
        _spinner.value = false //progressBar
    }

    private fun onErrorDownloading() {
        _status.value = CarsApiStatus.ERROR
        _spinner.value = false //progressBar
    }

    private fun onStartDownloading() {
        _status.value = CarsApiStatus.LOADING
        _spinner.value = true //progressBar
    }
}
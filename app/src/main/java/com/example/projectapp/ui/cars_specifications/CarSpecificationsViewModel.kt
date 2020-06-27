package com.example.projectapp.ui.cars_specifications

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectapp.domain.CarSpecifications
import com.example.projectapp.repository.CarRepository
import com.example.projectapp.ui.cars.CarsApiStatus
import com.example.projectapp.utils.SERVER_CONNECTION_ERROR
import com.example.projectapp.utils.singleArgViewModelFactory
import kotlinx.coroutines.launch
import java.io.IOException

class CarSpecificationsViewModel(private val repository: CarRepository) : ViewModel() {

    companion object {
        /**
         * Factory for creating [CarSpecificationsViewModel]
         *
         * @param arg the repository to pass to [CarSpecificationsViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::CarSpecificationsViewModel)
    }

    private val _status = MutableLiveData<CarsApiStatus>()
    val status: LiveData<CarsApiStatus>
        get() = _status

    private val _spinner = MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    private val _carDetails = MutableLiveData<CarSpecifications>()
    val carDetails: LiveData<CarSpecifications>
        get() = _carDetails

    //to check if the car is added to the favourite list or not .
    private val _favouriteStatus = MutableLiveData<Boolean>()
    val favouriteStatus: LiveData<Boolean>
        get() = _favouriteStatus
/*
    private val _ = MutableLiveData<Boolean>()
    val favouriteStatus: LiveData<Boolean>
        get() = _favouriteStatus
*/

    fun onToastShown() {
        _toast.value = null
    }

    fun loadCarSpecificationsById(userId: String, carId: Long) = launchDataLoad(userId, carId) {
        repository.getCarSpecificationsById(carId)
    }

    fun addCarToFavouriteList(userId: String, carId: Long) {
        viewModelScope.launch {
            try {
                onStartDownloading()
                repository.addCarToFavouriteList(userId, carId)
            } catch (error: IOException) {
                _toast.value = SERVER_CONNECTION_ERROR
                onErrorDownloading()
            } finally {
                onDoneDownloading()
            }
        }
    }

    fun removeCarFromFavouriteList(userId: String, carId: Long) {
        viewModelScope.launch {
            try {
                onStartDownloading()
                repository.removeCarFromFavouriteList(userId, carId)
            } catch (error: IOException) {
                _toast.value = SERVER_CONNECTION_ERROR
                onErrorDownloading()
            } finally {
                onDoneDownloading()
            }
        }
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
    private fun launchDataLoad(userId: String, carId: Long, block: suspend () -> CarSpecifications?) {
        /*
        The library adds a viewModelScope as an extension function of the ViewModel class.
        This scope is bound to Dispatchers.Main and will automatically be cancelled when the ViewModel is cleared.
         */
        viewModelScope.launch {
            try {
                onStartDownloading()
                _carDetails.value = block()
                _favouriteStatus.value = repository.getCarFavouriteStatus(userId, carId)

            } catch (error: IOException) {
                _toast.value = SERVER_CONNECTION_ERROR
                _carDetails.value = null
                onErrorDownloading()
            } finally {
                onDoneDownloading()
            }
        }
    }

    private fun onDoneDownloading() {
        _status.value = CarsApiStatus.DONE
        _spinner.value = false
    }

    private fun onErrorDownloading() {
        _status.value = CarsApiStatus.ERROR
        _spinner.value = false
    }

    private fun onStartDownloading() {
        _status.value = CarsApiStatus.LOADING
        _spinner.value = true
    }

}
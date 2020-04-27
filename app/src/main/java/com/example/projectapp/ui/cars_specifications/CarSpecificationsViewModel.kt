package com.example.projectapp.ui.cars_specifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectapp.domain.CarSpecifications
import com.example.projectapp.repository.CarRepository
import com.example.projectapp.ui.cars.CarsApiStatus
import com.example.projectapp.utils.singleArgViewModelFactory
import kotlinx.coroutines.launch

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

    private val _car = MutableLiveData<CarSpecifications>()
    val car: LiveData<CarSpecifications>
        get() = _car

    fun loadCarSpecificationsById(id: Long) = launchDataLoad {
        repository.getCarSpecificationsById(id)
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
    private fun launchDataLoad(block: suspend () -> CarSpecifications?) {
        /*
        The library adds a viewModelScope as an extension function of the ViewModel class.
        This scope is bound to Dispatchers.Main and will automatically be cancelled when the ViewModel is cleared.
         */
        viewModelScope.launch {
            try {
                onStartDownloading()
                _spinner.value = true //progressBar
                _car.value = block()
                onDoneDownloading()
            } catch (error: CarRepository.CarFetchingError) {
                _toast.value = error.message
                _car.value = null
                onErrorDownloading()
            } finally {
                _spinner.value = false
            }
        }
    }

    private fun onDoneDownloading() {
        _status.value = CarsApiStatus.DONE
    }

    private fun onErrorDownloading() {
        _status.value = CarsApiStatus.ERROR
    }

    private fun onStartDownloading() {
        _status.value = CarsApiStatus.LOADING
    }

}
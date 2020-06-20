package com.example.projectapp.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectapp.repository.CarRepository
import com.example.projectapp.repository.UserRepository
import com.example.projectapp.ui.cars.CarsViewModel
import com.example.projectapp.utils.singleArgViewModelFactory
import kotlinx.coroutines.launch
import java.io.IOException

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    companion object {
        /**
         * Factory for creating [ProfileViewModel]
         *
         * @param arg the repository to pass to [ProfileViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::ProfileViewModel)
    }

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    fun onDeleteAccountBtnClicked(userId: String) = launchDataLoad {
        repository.deleteAccount(userId)
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
                //here we don't need to assing _car = block() because we are observing the cars from repository.
                block()
                _toast.value = "Deleted Successfully"
            } catch (error: IOException) {
                _toast.value = null
            } finally {

            }
        }
    }
}
package com.example.projectapp.ui.account.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectapp.domain.User
import com.example.projectapp.repository.UserRepository
import com.example.projectapp.utils.singleArgViewModelFactory
import kotlinx.coroutines.launch
import java.io.IOException

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    companion object {
        /**
         * Factory for creating [RegisterViewModel]
         *
         * @param arg the repository to pass to [RegisterViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::RegisterViewModel)
    }

    enum class RegistrationState {
        //COLLECT_PROFILE_DATA,
        //COLLECT_USER_PASSWORD,
        COLLECT_USER_CREDENTIALS,
        REGISTRATION_COMPLETED
    }


    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    private val _spinner = MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _registrationState = MutableLiveData<RegistrationState>(RegistrationState.COLLECT_USER_CREDENTIALS)
    val registrationState: LiveData<RegistrationState>
        get() = _registrationState

    // Simulation of real-world scenario, where an auth token may be provided as
    // an alternate authentication mechanism instead of passing the password
    // around. This is set at the end of the registration process.
    var authToken: String? = ""
        private set

    fun collectProfileData(name: String, email: String, password: String) {
        // ... validate and store data

        // Change State to collecting username and password
        _registrationState.value = RegistrationState.COLLECT_USER_CREDENTIALS
    }

    /*
//called when register btn clicked
    fun createAccountAndLogin(email: String, password: String) {
        // ... create account
        // ... authenticate
        this.authToken = "the token"
        // Change State to registration completed
        registrationState.value = RegistrationState.REGISTRATION_COMPLETED
    }*/

    fun userCancelledRegistration(): Boolean {
        // Clear existing registration data
        _registrationState.value = RegistrationState.COLLECT_USER_CREDENTIALS
        authToken = ""
        return true
    }


    /**
     * Respond to onClick events .
     *
     * Create new account and login.
     *
     * The loading spinner will display until a result is returned, and errors will trigger
     * a toast.
     */
    fun onCreateNewAccountBtnClicked(name: String, email: String, password: String) = launchDataLoad {
        userRepository.createNewAccount(name, email, password)
    }

    fun userRegisteredAndLoginSuccessfully() {
        //Assign the verification token.
        this.authToken = _user.value?.verificationToken
        // Change State to registration completed
        //_registrationState.value = RegistrationState.REGISTRATION_COMPLETED
    }

    fun onToastShown() {
        _toast.value = null
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
    private fun launchDataLoad(block: suspend () -> User?) {
        /*
        The library adds a viewModelScope as an extension function of the ViewModel class.
        This scope is bound to Dispatchers.Main and will automatically be cancelled when the ViewModel is cleared.
         */
        viewModelScope.launch {
            try {
                onStartLoading()
                _user.value = block()
                if (_user.value != null) {
                    _registrationState.value = RegistrationState.REGISTRATION_COMPLETED
                    _toast.value = "Registered Successfully"
                } else {
                    _registrationState.value = RegistrationState.COLLECT_USER_CREDENTIALS
                    _toast.value = "Please check your information !"
                }
                onFinishLoading()
            } catch (error: IOException) {
                _toast.value = "Unable to connect to the server !"
                _registrationState.value = RegistrationState.COLLECT_USER_CREDENTIALS
                onErrorLoading()
            } finally {
                onFinishLoading()
            }
        }
    }

    /**
     * Show a ProgressBar
     */
    private fun onStartLoading() {
        _spinner.value = true
    }

    /**
     * Hide the ProgressBar
     */
    private fun onFinishLoading() {
        _spinner.value = false
    }

    /**
     * Hide the ProgressBar
     */
    private fun onErrorLoading() {
        _spinner.value = false
    }
}
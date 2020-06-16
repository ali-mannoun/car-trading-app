package com.example.projectapp.ui.account.login

import android.util.Log
import androidx.lifecycle.*
import com.example.projectapp.domain.User
import com.example.projectapp.repository.UserRepository
import com.example.projectapp.utils.singleArgViewModelFactory
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    companion object {
        /**
         * Factory for creating [LoginViewModel]
         *
         * @param arg the repository to pass to [LoginViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::LoginViewModel)
    }

    enum class AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED,        // The user has authenticated successfully
        INVALID_AUTHENTICATION,  // Authentication failed
        AUTHENTICATED_AND_REMEMBER_ME //remember me
    }

    private val SUCCESS_RESPONSE = 200

    private val _authenticationState = MutableLiveData<AuthenticationState>()
    val authenticationState: LiveData<AuthenticationState>
        get() = _authenticationState

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    private val _spinner = MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean>
        get() = _spinner

    init {
        Log.e("LoginViewModel", "Constructor called")
        //the user is always unauthenticated when MainActivity is launched [default]
        _authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    fun authenticateAndLogin(email: String, password: String) = launchDataLoad {
        userRepository.login(email, password)
    }

    fun authenticate(authCode: String) {
        //here we don't need to put onStartLoading() becuase we are running this method in RegisterFragment.
        viewModelScope.launch {
            try {
                //Check if verificationToken is valid.
                val isVerified = userRepository.verifyAccount(authCode)
                if (isVerified) {
                     _toast.value = "Verified Successfully"
                    _authenticationState.value = AuthenticationState.AUTHENTICATED
                } else {
                    _toast.value = "Invalid verification token"
                    _authenticationState.value = AuthenticationState.UNAUTHENTICATED
                }
            } catch (error: IOException) {
                _toast.value = "Unable to connect to the server !"
                _authenticationState.value = AuthenticationState.UNAUTHENTICATED
            } finally {
            }
        }
    }

    fun refuseAuthentication() {
        _authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    fun rememberUser(rememberMe: Boolean) {
        if (rememberMe) {
            _authenticationState.value = AuthenticationState.AUTHENTICATED_AND_REMEMBER_ME
        }
    }

    /**
     * Respond to onClick events .
     *
     * The loading spinner will display until a result is returned, and errors will trigger
     * a toast.
     */
    private fun onLoginBtnClicked(email: String, password: String) = launchDataLoad {
        userRepository.login(email, password)
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
                    _authenticationState.value = AuthenticationState.AUTHENTICATED
                    _toast.value = "Authenticated Successfully"
                } else {
                    _authenticationState.value = AuthenticationState.UNAUTHENTICATED
                    _toast.value = "Please check your information !"
                }
                onFinishLoading()
            } catch (error: IOException) {
                _toast.value = "Unable to connect to the server !"
                _authenticationState.value = AuthenticationState.UNAUTHENTICATED
                onErrorLoading()
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
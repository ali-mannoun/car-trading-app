package com.example.projectapp.ui.account

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.projectapp.R
import com.example.projectapp.domain.User
import com.example.projectapp.ui.LoadingBottomSheetDialog
import com.example.projectapp.ui.account.login.LoginViewModel
import com.example.projectapp.ui.account.register.RegisterViewModel
import com.example.projectapp.utils.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

fun ViewModel.observeLoadingStatus(viewLifecycleOwner: LifecycleOwner,
                                   button: MaterialButton,
                                   spinner: LiveData<Boolean>,
                                   bottomSheet: LoadingBottomSheetDialog,
                                   parentFragmentManager: FragmentManager,
                                   tag: String) {

    //Show Loading spinner while the data is processing...
    spinner.observe(viewLifecycleOwner, Observer {
        if (it) {
            button.isEnabled = false
            bottomSheet.show(parentFragmentManager, tag)
        } else {
            button.isEnabled = true
            bottomSheet.dismiss()
        }
    })
}

fun ViewModel.observeToastMessage(viewLifecycleOwner: LifecycleOwner,
                                  toast: LiveData<String?>,
                                  view: View, tag: String) {

    // when the server isn't responding show the error message.
    toast.observe(viewLifecycleOwner, Observer { message: String? ->
        Log.e("toast observe", message.toString())
        //Null-check is very important here.
        if (message != null) {
            Snackbar.make(view, message.toString(), Snackbar.LENGTH_LONG).show()
            if (tag == LOGIN_TAG) {
                (this as LoginViewModel).onToastShown()
            } else if (tag == REGISTER_TAG) {
                (this as RegisterViewModel).onToastShown()
            }
        }
    })
}

//when the user register new account we verify the account , if success then we pop backstack to the car fragment
fun ViewModel.observerToastVerificationStatus(viewLifecycleOwner: LifecycleOwner, navController: NavController, view: View, toast: LiveData<String?>) {
    toast.observe(viewLifecycleOwner, Observer {
        if (it == VERIFIED_SUCCESSFULLY) {
            Snackbar.make(view, it.toString(), Snackbar.LENGTH_LONG).show()
            navController.popBackStack(R.id.nav_cars_menu, false)
        }
    })
}

fun addUserPrefs(context: Context, user: User) {
    val sp: SharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sp.edit()
    editor.putString(USER_ID, user.id)
    editor.putString(USER_NAME, user.name)
    editor.putString(USER_EMAIL, user.email)
    editor.apply()
}

fun ViewModel.observeRegistrationState(viewLifecycleOwner: LifecycleOwner,
                                       registrationState: LiveData<RegisterViewModel.RegistrationState>,
                                       view: View, context: Context,loginViewModel: LoginViewModel,
                                       emailInputLayout: TextInputLayout) {
    /**
     * RegistrationViewModel updates the registrationState to REGISTRATION_COMPLETED when ready, and for this example,
     * the username is accessed as a read-only property from RegistrationViewModel and is used to directly authenticate with loginViewModel.
     */
    registrationState.observe(viewLifecycleOwner, Observer { state: RegisterViewModel.RegistrationState ->
        Log.e("RegisterFragment", "register state observer called { $state }")

        if (state == RegisterViewModel.RegistrationState.REGISTRATION_COMPLETED) { //User registered successfully
            /**
             * Here we authenticate with the token provided by the ViewModel then pop back to the profie_fragment,
             * where the user authentication status will be tested and should be authenticated.
             */
            Snackbar.make(view, context.getString(R.string.verifying_your_account), Snackbar.LENGTH_LONG).show()
            loginViewModel.authenticate((this as RegisterViewModel).authToken)
            addUserPrefs(context, (this as RegisterViewModel).user!!)

        } else if (state == RegisterViewModel.RegistrationState.COLLECT_USER_CREDENTIALS) {
            //User not registered so we return back to COLLECT_USER_CREDENTIALS state
            emailInputLayout.error = context.getString(R.string.email_address_already_exist)
            //remove the rememberMe value from prefs.
            removeRememberMePrefs(context)
        }
    })
}

fun ViewModel.observeLoginAuthenticationState(viewLifecycleOwner: LifecycleOwner, navController: NavController,
                                              authenticationState: LiveData<LoginViewModel.AuthenticationState>,
                                              view: View, context: Context,emailInputLayout: TextInputLayout) {
    //detect authentication state
    authenticationState.observe(viewLifecycleOwner, Observer { it ->
        Log.e("LoginFragment", "authentication observer called { $it }")
        val user = (this as LoginViewModel).user
        //by default we start the CarFragment but if the user is unauthenticated then we start LoginFragment and then pop it from the stack.
        when (it) {
            LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                addUserPrefs(context, user!!)
                navController.popBackStack()
            }
            LoginViewModel.AuthenticationState.AUTHENTICATED_AND_REMEMBER_ME -> {
                addUserPrefs(context, user!!)
                navController.popBackStack()
            }
            LoginViewModel.AuthenticationState.INVALID_AUTHENTICATION -> {
                emailInputLayout.error = context.getString(R.string.incorrect_email_or_password)
                //remove the rememberMe value from prefs.
                removeRememberMePrefs(context)
                Snackbar.make(view, context.getString(R.string.login_to_continue), Snackbar.LENGTH_SHORT).show()
            }
            else -> Snackbar.make(view, context.getString(R.string.login_to_continue), Snackbar.LENGTH_SHORT).show()
        }
    })
}

fun ViewModel.observeAuthenticationStateInCarsFragment(viewLifecycleOwner: LifecycleOwner,
                                                       authenticationState: LiveData<LoginViewModel.AuthenticationState>
                                                       , navController: NavController, block: () -> Unit) {

    //If the user reach this fragment and isn't authenticated then we move it to the LoginFragment, else continue.
    authenticationState.observe(viewLifecycleOwner, Observer { it ->
        Log.e("CarsFragment", "login car state observed { $it }")

        if (it == LoginViewModel.AuthenticationState.UNAUTHENTICATED) {
            navController.navigate(R.id.loginFragment)
        } else {
            block()
        }
    })
}
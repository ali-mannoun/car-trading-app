package com.example.projectapp.ui.account.register

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.projectapp.R
import com.example.projectapp.databinding.FragmentRegisterBinding
import com.example.projectapp.network.getNetworkService
import com.example.projectapp.repository.UserRepository
import com.example.projectapp.sharedViewModel
import com.example.projectapp.ui.LoadingBottomSheetDialog
import com.example.projectapp.ui.account.login.LoginViewModel
import com.example.projectapp.utils.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Pattern
import kotlin.math.log

const val USER_ID = "id"
const val USER_NAME = "name"
const val USER_EMAIL = "email"

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var bottomSheet: LoadingBottomSheetDialog
    private var allInputFieldsValidated = false

    val USER_ID = "id"
    val USER_NAME = "name"
    val USER_EMAIL = "email"

    private val registerViewModel: RegisterViewModel by viewModels<RegisterViewModel>(
            factoryProducer = {
                RegisterViewModel.FACTORY(UserRepository(getNetworkService()))
            }
    )

    private val loginViewModel: LoginViewModel by activityViewModels(
            factoryProducer = {
                LoginViewModel.FACTORY(UserRepository(getNetworkService()))
            }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //To hide the BottomNavigationView
        sharedViewModel.setBottomNavigationViewVisibility(isVisible = false)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        binding.username.doOnTextChanged { text, _, _, _ ->
            if (text!!.length < 3) {
                allInputFieldsValidated = false
                binding.userNameInputLayout.error = "At least 3 characters !"
            } else {
                allInputFieldsValidated = true
                binding.userNameInputLayout.error = null
            }
        }

        binding.email.doOnTextChanged { text, _, _, _ ->
            val emailPattern: Pattern = Patterns.EMAIL_ADDRESS
            val isValid = emailPattern.matcher(text.toString()).matches()
            if (!isValid) {
                allInputFieldsValidated = false
                binding.emailInputLayout.error = "Invalid Email !"
            } else {
                allInputFieldsValidated = true
                binding.emailInputLayout.error = null
            }
        }

        binding.password.doOnTextChanged { text, _, _, _ ->
            if (text!!.length < 8) {
                allInputFieldsValidated = false
                binding.passwordInputLayout.error = "Must at least 8 characters!"
            } else {
                allInputFieldsValidated = true
                binding.passwordInputLayout.error = null
            }
        }

        binding.confirmPassword.doOnTextChanged { text, _, _, _ ->
            if (binding.password.text.toString() != text.toString()) {
                allInputFieldsValidated = false
                binding.confirmPasswordInputLayout.error = "Password doesn't match!"
            } else {
                allInputFieldsValidated = true
                binding.confirmPasswordInputLayout.error = null
            }
        }

        binding.createNewAccountBtn.setOnClickListener { view: View ->
            val name = binding.username.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val passwordConfirm = binding.confirmPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                Toast.makeText(context, "empty not allowed", Toast.LENGTH_SHORT).show()
            } else if (allInputFieldsValidated) {
                if (binding.rememberMeCheckBox.isChecked) {
                    //if the rememberMe checked ,then don't show RegisterFragment again.
                    val sp: SharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sp.edit()
                    editor.putBoolean("rememberMeChecked", true)
                    editor.apply()
                }
                bottomSheet = LoadingBottomSheetDialog()
                bottomSheet.isCancelable = false

                registerViewModel.onCreateNewAccountBtnClicked(name, email, password)
            }
        }

        registerViewModel.user.observe(viewLifecycleOwner, androidx.lifecycle.Observer { user ->
            if (user != null) {
                //user registered successfully
                registerViewModel.userRegisteredAndLoginSuccessfully()
                //store user in prefs
                val sp: SharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sp.edit()
                editor.putString(USER_ID, user.id)
                editor.putString(USER_NAME, user.name)
                editor.putString(USER_EMAIL, user.email)
                editor.apply()
            } else {
                //remove the rememberMe value from prefs.
                val sp: SharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sp.edit()
                editor.remove("rememberMeChecked")
                editor.apply()
                //the user is already exists.
                binding.emailInputLayout.error = "Email address already exists !"
            }
        })
        //control progressbar visibility.
        registerViewModel.spinner.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.createNewAccountBtn.isEnabled = false
                bottomSheet.show(childFragmentManager, "RegisterFragment...")
            } else {
                binding.createNewAccountBtn.isEnabled = true
                bottomSheet.dismiss()
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()

        // If the user presses back, cancel the user registration and pop back
        // to the login fragment. Since this ViewModel is shared at the activity
        // scope, its state must be reset so that it will be in the initial
        // state if the user comes back to register later.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            registerViewModel.userCancelledRegistration()
            navController.popBackStack(R.id.loginFragment, false)
        }

        // RegistrationViewModel updates the registrationState to
        // REGISTRATION_COMPLETED when ready, and for this example, the username
        // is accessed as a read-only property from RegistrationViewModel and is
        // used to directly authenticate with loginViewModel.
        registerViewModel.registrationState.observe(viewLifecycleOwner, Observer { state ->
            if (state == RegisterViewModel.RegistrationState.REGISTRATION_COMPLETED) {
                // Here we authenticate with the token provided by the ViewModel
                // then pop back to the profie_fragment, where the user authentication
                // status will be tested and should be authenticated.
                Snackbar.make(binding.root, "Verifying your account... please wait !", Snackbar.LENGTH_LONG).show()
                val authToken = registerViewModel.authToken
                loginViewModel.authenticate(authToken!!)
            }
        })

        loginViewModel.toast.observe(viewLifecycleOwner, Observer {
            Snackbar.make(binding.root, it.toString(), Snackbar.LENGTH_LONG).show()
            findNavController().popBackStack(R.id.nav_cars_menu, false)
        })
    }
}
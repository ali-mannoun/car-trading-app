package com.example.projectapp.ui.account.login

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
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.projectapp.R
import com.example.projectapp.databinding.FragmentLoginBinding
import com.example.projectapp.network.getNetworkService
import com.example.projectapp.repository.UserRepository
import com.example.projectapp.sharedViewModel
import com.example.projectapp.ui.LoadingBottomSheetDialog
import com.example.projectapp.ui.account.register.USER_EMAIL
import com.example.projectapp.ui.account.register.USER_ID
import com.example.projectapp.ui.account.register.USER_NAME
import com.example.projectapp.utils.CheckNetworkConnectivity
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Pattern

//private var maxAttemptsToPressBackKey = 1

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val bottomSheet = LoadingBottomSheetDialog()
    private var allInputFieldsValidated = false

    //we set the activityViewModels() to share data between fragments shared in the same host activity.
    private val viewModel: LoginViewModel by activityViewModels(
            factoryProducer = {
                LoginViewModel.FACTORY(UserRepository(getNetworkService()))
            }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        sharedViewModel.setBottomNavigationViewVisibility(isVisible = false)
        sharedViewModel.setActiveIntroStarted(isVisible = false)

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

        binding.loginBtn.setOnClickListener { view: View? ->
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Empty fields not allowed", Toast.LENGTH_SHORT).show()
            } else if (!CheckNetworkConnectivity.isOnline(requireNotNull(context))) {
                Toast.makeText(context, "No internet connection !", Toast.LENGTH_SHORT).show()
            } else if (allInputFieldsValidated) {
                if (binding.rememberMeCheckBox.isChecked) {
                    //Don't show the LoginFragment again, and process to the CarFragment.
                    val sp: SharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sp.edit()
                    editor.putBoolean("rememberMeChecked", true)
                    editor.apply()
                }
                viewModel.authenticate(email, password)
                bottomSheet.isCancelable = false
            }
        }

        binding.createNewAccountBtn.setOnClickListener { view: View ->
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            view.findNavController().navigate(action)
        }

        //Show Loading spinner while the data is processing...
        viewModel.spinner.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.loginBtn.isEnabled = false
                bottomSheet.show(childFragmentManager, "LoginFragment...")
            } else {
                binding.loginBtn.isEnabled = true
                bottomSheet.dismiss()
            }
        })

        //when the server isn't responding show the error message.
        viewModel.toast.observe(viewLifecycleOwner, Observer { message: String? ->
            if (message != null) {
                Toast.makeText(context, "Something went wrong, please try again !", Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        })
        return binding.root
    }

    private fun showLoginErrorMessage() {
        Snackbar.make(binding.root, "Please login to continue", Snackbar.LENGTH_SHORT).show()
    }

    private fun processLogin() {
        if (bottomSheet.isVisible) {
            bottomSheet.dismiss()
        }
        //by default we start the CarFragment but if the user is unauthenticated then we start LoginFragment and then pop it from the stack.
       // findNavController().popBackStack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
            //viewModel.refuseAuthentication()
        }

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> processLogin()
                LoginViewModel.AuthenticationState.AUTHENTICATED_AND_REMEMBER_ME -> processLogin()
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> showLoginErrorMessage()
                else -> showLoginErrorMessage()
            }
        })

        viewModel.user.observe(viewLifecycleOwner, Observer { user ->

            if (user != null) {

                Toast.makeText(context, user.name, Toast.LENGTH_SHORT).show()
                //store user in prefs
                val sp: SharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sp.edit()
                editor.putString(USER_ID, user.id)
                editor.putString(USER_NAME, user.name)
                editor.putString(USER_EMAIL, user.email)
                editor.apply()

                findNavController().popBackStack()
            } else {

                //remove the data stored in prefs if rememberMe checkBox is checked.
                val sp: SharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sp.edit()
                editor.remove("rememberMeChecked")
                editor.apply()

                binding.emailInputLayout.error = "Incorrect Email or Password"
                Snackbar.make(binding.root, "Please check your information !", Snackbar.LENGTH_LONG).show()
            }
        })
    }
}
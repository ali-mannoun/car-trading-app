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
import com.example.projectapp.ui.account.*
import com.example.projectapp.ui.account.login.LoginViewModel
import com.example.projectapp.utils.*
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Pattern

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val bottomSheet = LoadingBottomSheetDialog()
    private var allInputFieldsValidated = false
    private val userRepository = UserRepository(getNetworkService())
    private val registerViewModel: RegisterViewModel by viewModels(
            factoryProducer = {
                RegisterViewModel.FACTORY(userRepository)
            }
    )
    private val loginViewModel: LoginViewModel by activityViewModels(
            factoryProducer = {
                LoginViewModel.FACTORY(userRepository)
            }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //Hide the BottomNavigationView
        sharedViewModel.setBottomNavigationViewVisibility(isVisible = false)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        validateFields()

        binding.createNewAccountBtn.setOnClickListener { view: View ->
            binding.userNameInputLayout.error = null
            binding.emailInputLayout.error = null
            binding.passwordInputLayout.error = null
            binding.confirmPasswordInputLayout.error = null

            val name = binding.username.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val passwordConfirm = binding.confirmPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                Toast.makeText(context, getString(R.string.empty_fields_not_allowed), Toast.LENGTH_SHORT).show()
            } else if (!CheckNetworkConnectivity.isOnline(requireNotNull(context))) {
                Toast.makeText(context, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
            } else if (allInputFieldsValidated) {
                if (binding.rememberMeCheckBox.isChecked) {
                    addRememberMePrefs(requireContext())
                }
                registerViewModel.onCreateNewAccountBtnClicked(name, email, password)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()

        /**
         * If the user presses back, cancel the user registration and pop back to the login fragment.
         * Since this ViewModel is shared at the activity scope, its state must be reset so that it will be in the initial state
         * if the user comes back to register later.
         */
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            registerViewModel.userCancelledRegistration()
            navController.popBackStack(R.id.loginFragment, false)
        }

        //registerViewModel.registrationState
        registerViewModel.observeRegistrationState(viewLifecycleOwner, registerViewModel.registrationState,
                binding.root, requireContext(), loginViewModel, binding.emailInputLayout)

        //loginViewModel.toast
        loginViewModel.observerToastVerificationStatus(viewLifecycleOwner, findNavController(), binding.root, loginViewModel.toast)

        //registerViewModel.toast
        registerViewModel.observeToastMessage(viewLifecycleOwner, registerViewModel.toast, binding.root, REGISTER_TAG)

        //registerViewModel.spinner
        registerViewModel.observeLoadingStatus(viewLifecycleOwner, binding.createNewAccountBtn, registerViewModel.spinner,
                bottomSheet,
                parentFragmentManager, REGISTER_TAG)
    }

    private fun validateFields() {
        binding.userNameInputLayout.error = null
        binding.emailInputLayout.error = null
        binding.passwordInputLayout.error = null
        binding.confirmPasswordInputLayout.error = null

        binding.username.doOnTextChanged { text, _, _, _ ->
            if (text!!.length < 3) {
                allInputFieldsValidated = false
                binding.userNameInputLayout.error = getString(R.string.at_least_three_characters)
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
                binding.emailInputLayout.error = getString(R.string.invalid_email)
            } else {
                allInputFieldsValidated = true
                binding.emailInputLayout.error = null
            }
        }

        binding.password.doOnTextChanged { text, _, _, _ ->
            if (text!!.length < 8) {
                allInputFieldsValidated = false
                binding.passwordInputLayout.error = getString(R.string.must_at_least_eight_characters)
            } else {
                allInputFieldsValidated = true
                binding.passwordInputLayout.error = null
            }
        }

        binding.confirmPassword.doOnTextChanged { text, _, _, _ ->
            if (binding.password.text.toString() != text.toString()) {
                allInputFieldsValidated = false
                binding.confirmPasswordInputLayout.error = getString(R.string.password_doesnot_match)
            } else {
                allInputFieldsValidated = true
                binding.confirmPasswordInputLayout.error = null
            }
        }

    }
}
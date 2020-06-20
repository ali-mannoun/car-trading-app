package com.example.projectapp.ui.account.login

import android.os.Bundle
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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.projectapp.R
import com.example.projectapp.databinding.FragmentLoginBinding
import com.example.projectapp.network.getNetworkService
import com.example.projectapp.repository.UserRepository
import com.example.projectapp.sharedViewModel
import com.example.projectapp.ui.LoadingBottomSheetDialog
import com.example.projectapp.ui.account.observeLoadingStatus
import com.example.projectapp.ui.account.observeLoginAuthenticationState
import com.example.projectapp.ui.account.observeToastMessage
import com.example.projectapp.utils.*
import java.util.regex.Pattern

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val bottomSheet = LoadingBottomSheetDialog()
    private var allInputFieldsValidated = false

    //we set the activityViewModels() to share data between fragments shared in the same host activity.
    //You can share data between the fragments by having a ViewModel scoped to the activity, which implements ViewModelStoreOwner.
    private val viewModel: LoginViewModel by activityViewModels(
            factoryProducer = {
                LoginViewModel.FACTORY(UserRepository(getNetworkService()))
            }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        //To hide BottomNavigationView and toolbar when loading LoginFragment.
        sharedViewModel.setBottomNavigationViewVisibility(isVisible = false)
        sharedViewModel.setActiveIntroStarted(isVisible = false)

        validateFields()

        binding.loginBtn.setOnClickListener { view: View? ->
            binding.emailInputLayout.error = null
            binding.passwordInputLayout.error = null

            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, getString(R.string.empty_fields_not_allowed), Toast.LENGTH_SHORT).show()
            } else if (!CheckNetworkConnectivity.isOnline(requireNotNull(context))) {
                Toast.makeText(context, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
            } else if (allInputFieldsValidated) {
                if (binding.rememberMeCheckBox.isChecked) {
                    addRememberMePrefs(requireContext())
                }
                viewModel.authenticateAndLogin(email, password)
            }
        }

        binding.createNewAccountBtn.setOnClickListener { view: View ->
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            view.findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.refuseAuthentication()
            requireActivity().finish()
        }

        //viewModel.spinner
        viewModel.observeLoadingStatus(viewLifecycleOwner, binding.loginBtn, viewModel.spinner,
                bottomSheet, parentFragmentManager, LOGIN_TAG)

        //viewModel.toast
        viewModel.observeToastMessage(viewLifecycleOwner, viewModel.toast, binding.root, LOGIN_TAG)

        //viewModel.authenticationState
        viewModel.observeLoginAuthenticationState(viewLifecycleOwner, findNavController(),
                viewModel.authenticationState, binding.root, requireContext()
                , binding.emailInputLayout)
    }

    private fun validateFields() {
        binding.emailInputLayout.error = null
        binding.passwordInputLayout.error = null

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
    }
}
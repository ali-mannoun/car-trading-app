package com.example.projectapp.ui.account.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.projectapp.R
import com.example.projectapp.databinding.FragmentRegisterBinding
import com.example.projectapp.network.getNetworkService
import com.example.projectapp.repository.UserRepository
import com.example.projectapp.sharedViewModel
import com.example.projectapp.ui.account.login.LoginViewModel
import com.example.projectapp.utils.SharedViewModel

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

     private val registerViewModel: RegisterViewModel by activityViewModels<RegisterViewModel>(
             factoryProducer = {
                 RegisterViewModel.FACTORY(UserRepository(getNetworkService()))
             }
     )

    private val loginViewModel: LoginViewModel by activityViewModels()

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        sharedViewModel.setBottomNavigationViewVisibility(false)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.createNewAccountBtn.setOnClickListener { view: View ->
            val name = binding.username.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val passwordConfirm = binding.confirmPassword.text.toString()

            if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                //TODO add pattern to check the email regex
                //TODO add functionaloty to send verification token via email OR ANY OTHER METHOD IN ANDROID
                Toast.makeText(context, "empty not allowed", Toast.LENGTH_SHORT).show()
            } else if (!password.equals(passwordConfirm)) {
                Toast.makeText(context, "password doesn't match!", Toast.LENGTH_SHORT).show()
            } else {
                // RegistrationViewModel updates the registrationState to
                // REGISTRATION_COMPLETED when ready, and for this example, the username
                // is accessed as a read-only property from RegistrationViewModel and is
                // used to directly authenticate with loginViewModel.
                registerViewModel.registrationState.observe(
                        viewLifecycleOwner, Observer { state ->
                    if (state == RegisterViewModel.RegistrationState.REGISTRATION_COMPLETED) {
                        // Here we authenticate with the token provided by the ViewModel
                        // then pop back to the profie_fragment, where the user authentication
                        // status will be tested and should be authenticated.
                        val authToken = registerViewModel.authToken
                        //todo loginViewModel.authenticate(authToken)
                        findNavController().popBackStack(R.id.nav_cars_menu, false)
                    }
                }
                )

                registerViewModel.onCreateNewAccountBtnClicked(name, email, password)
            }
        }

        registerViewModel.user.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                Toast.makeText(context, it.name, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "false", Toast.LENGTH_LONG).show()
            }
        })
        return binding.root
    }
}
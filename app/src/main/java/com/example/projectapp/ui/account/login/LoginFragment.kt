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
import com.example.projectapp.utils.CheckNetworkConnectivity
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Pattern

private var maxAttemptsToPressBackKey = 1

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var bottomSheet: LoadingBottomSheetDialog

    private val viewModel: LoginViewModel by activityViewModels(
            factoryProducer = {
                LoginViewModel.FACTORY(UserRepository(getNetworkService()))
            }
    )

    private fun showErrorMessage() {
        Toast.makeText(context, "Please login to continue", Toast.LENGTH_LONG).show()
    }

    private fun processLogin() {
        if (bottomSheet.isVisible) {
            bottomSheet.dismiss()
        }
        findNavController().popBackStack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (maxAttemptsToPressBackKey > 0) {
                maxAttemptsToPressBackKey--
                viewModel.refuseAuthentication()
                findNavController().popBackStack(R.id.nav_cars_menu, false)
            } else {
                requireActivity().finish()
            }
        }

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> processLogin()
                LoginViewModel.AuthenticationState.AUTHENTICATED_AND_REMEMBER_ME -> processLogin()
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> showErrorMessage()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        sharedViewModel.setBottomNavigationViewVisibility(false)
        sharedViewModel.setActiveIntroStarted(false)

            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

            binding.email.doOnTextChanged { text, _, _, _ ->
                val emailPattern: Pattern = Patterns.EMAIL_ADDRESS
                val isValid = emailPattern.matcher(text.toString()).matches()
                if (!isValid) {
                    binding.emailInputLayout.error = "Invalid Email !"
                } else {
                    binding.emailInputLayout.error = null
                }
            }

            binding.password.doOnTextChanged { text, _, _, _ ->
                if (text!!.length < 8) {
                    binding.passwordInputLayout.error = "Must at least 8 characters!"
                } else {
                    binding.passwordInputLayout.error = null
                }
            }

            binding.loginBtn.setOnClickListener { view: View? ->
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "empty not allowed", Toast.LENGTH_SHORT).show()
                } else if (!CheckNetworkConnectivity.isOnline(requireNotNull(context))) {
                    Toast.makeText(context, "No internet connection !", Toast.LENGTH_SHORT).show()
                } else {
                    if(binding.rememberMeCheckBox.isChecked){
                        val sp: SharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sp.edit()
                        editor.putBoolean("rememberMeChecked", true)
                        editor.apply()
                    }
                    bottomSheet = LoadingBottomSheetDialog()
                    bottomSheet.isCancelable = false
                    viewModel.authenticate(email, password)
                }
            }

            binding.createNewAccountBtn.setOnClickListener { view: View ->
                val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                view.findNavController().navigate(action)
            }

            viewModel.spinner.observe(viewLifecycleOwner, Observer {
                if (it) {
                    binding.loginBtn.isEnabled = false
                    bottomSheet.show(parentFragmentManager, "LoginFragment...")
                } else {
                    binding.loginBtn.isEnabled = true
                    bottomSheet.dismiss()
                }
            })

            viewModel.user.observe(viewLifecycleOwner, Observer { user ->
                if (user != null) {
                    Toast.makeText(context, user.name, Toast.LENGTH_SHORT).show()

                } else {
                    val sp: SharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sp.edit()
                    editor.remove("rememberMeChecked")
                    editor.apply()

                    binding.emailInputLayout.error = "Incorrect Email or Password"
                    Snackbar.make(binding.root, "Please check your information !", Snackbar.LENGTH_LONG).show();
                }
            })

            viewModel.toast.observe(viewLifecycleOwner, Observer { message: String? ->
                if (message != null) {
                    //Log.e("TOAST : ", message + " ...")
                    Toast.makeText(context, "Something went wrong, please try again !", Toast.LENGTH_SHORT).show()
                    viewModel.onToastShown()
                }
            })
            return binding.root
        }
}

/*
        sharedViewModel.getDialogPositiveClicked().observe(this, id -> {
            if (id != null) {
                Toast.makeText(getContext(), "onChanged", Toast.LENGTH_SHORT).show();
                sharedViewModel.dialogActionComplete();
            }
        });

        /*
        binding.password.setOnKeyListener((view, i, keyEvent) -> {
            if (binding.passwordInputLayout.getError() == null) {
                binding.passwordInputLayout.setError(null);
                if (binding.passwordInputLayout.getEditText().getText().toString().length() > 20) {
                    binding.passwordInputLayout.setError("Limited exceed !");
                    allDone = false;
                } else {
                    binding.passwordInputLayout.setError(null);
                    allDone = true;
                }
                binding.executePendingBindings();
            } else if (binding.passwordInputLayout.getEditText().getText().toString().length() <= 20) {
                binding.passwordInputLayout.setError(null);
                allDone = true;

            }
            return false;
        });

 */
/*
        binding.email.setOnKeyListener((view, i, keyEvent) -> {
//TODO add the email authentication
            if (binding.emailInputLayout.getError() == null) {

                binding.emailInputLayout.setError(null);
                if (binding.emailInputLayout.getEditText().getText().toString().length() > 20) {
                    binding.emailInputLayout.setError("Limited exceed !");
                    allDone = false;
                } else {
                    binding.emailInputLayout.setError(null);
                    allDone = true;
                }
                binding.executePendingBindings();
            } else if (binding.emailInputLayout.getEditText().getText().toString().length() <= 20) {
                binding.emailInputLayout.setError(null);
                allDone = true;

            }
            return false;
        });
*/
        return binding.root
    }
}*/
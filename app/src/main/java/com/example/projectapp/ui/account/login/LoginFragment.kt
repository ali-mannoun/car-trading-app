package com.example.projectapp.ui.account.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.projectapp.R
import com.example.projectapp.databinding.FragmentLoginBinding
import com.example.projectapp.network.getNetworkService
import com.example.projectapp.repository.UserRepository
import com.example.projectapp.ui.LoadingBottomSheetDialog

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var bottomSheet: LoadingBottomSheetDialog

    private val viewModel: LoginViewModel by viewModels(
            factoryProducer = {
                LoginViewModel.FACTORY(UserRepository(getNetworkService()))
            }
    )

    //private val sharedViewModel: SharedViewModel? = null
    //private val allDone = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        //  allDone = TestLoginFragmentFields.testPasswordField(binding);
//  allDone = TestLoginFragmentFields.testEmailField(binding);

        binding.loginBtn.setOnClickListener { view: View? ->
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            bottomSheet = LoadingBottomSheetDialog()
            //bottomSheet.show(parentFragmentManager, "LoginFragment...")
            bottomSheet.isCancelable = false
            //bottomSheet.dismiss()

            if (email.isEmpty() || password.isEmpty()) {
                //TODO add pattern to check the email regex
                Toast.makeText(context, "empty not allowed", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.onLoginBtnClicked(email, password)
            }
            /*
            if (allDone) {
                Navigation.findNavController(view!!).navigate(LoginFragmentDirections.actionLoginFragmentToUserDetailsActivity())
            }
             */
        }

        binding.createNewAccountBtn.setOnClickListener { view: View ->
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            view.findNavController().navigate(action)
        }

        viewModel.spinner.observe(viewLifecycleOwner, Observer {
            if (it) {
                bottomSheet.show(parentFragmentManager, "LoginFragment...")
            } else {
                bottomSheet.dismiss()
            }
        })

        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                Toast.makeText(context, user.name, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "No user found", Toast.LENGTH_SHORT).show()
            }
        })
        return binding.root
    }
}
/*
sharedViewModel.getSelected().observe(this, accountTypeLabel -> {
    if (accountTypeLabel != null) {
        if (accountTypeLabel.equals(getString(R.string.admin_account)) || accountTypeLabel.equals(getString(R.string.user_account))) {
            Navigation.findNavController(binding.getRoot()).navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment(accountTypeLabel));
        } else if (accountTypeLabel.equals(getString(R.string.employee_account))) {
            //DialogFragment dialog = new EmployeeDialogFragment();
            //  dialog.show(getFragmentManager(), "EmployeeDialogFragment");
        }
        sharedViewModel.navigationComplete();
    }
});
*/
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
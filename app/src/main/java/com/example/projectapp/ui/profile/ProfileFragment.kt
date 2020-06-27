package com.example.projectapp.ui.profile

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.projectapp.R
import com.example.projectapp.databinding.FragmentProfileBinding
import com.example.projectapp.network.getNetworkService
import com.example.projectapp.repository.UserRepository
import com.example.projectapp.sharedViewModel
import com.example.projectapp.ui.account.login.LoginViewModel
import com.example.projectapp.utils.REMEMBER_ME
import com.example.projectapp.utils.USER_EMAIL
import com.example.projectapp.utils.USER_ID
import com.example.projectapp.utils.USER_NAME

class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels(
            factoryProducer = {
                ProfileViewModel.FACTORY(UserRepository(getNetworkService()))
            }
    )
    private val loginViewModel: LoginViewModel by activityViewModels(
            factoryProducer = {
                LoginViewModel.FACTORY(UserRepository(getNetworkService()))
            }
    )

    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        sharedViewModel.setActiveIntroStarted(false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val sp: SharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sp.edit()

        val userId = sp.getString(USER_ID, "-1")
        val userName = sp.getString(USER_NAME, "-1")
        val email = sp.getString(USER_EMAIL, "-1")

        binding.userName.text = userName
        binding.email.text = email

        binding.deleteAccountBtn.setOnClickListener {
            AlertDialog.Builder(requireContext())
                    .setTitle("Delete your account")
                    .setMessage("Are you sure ?")
                    .setPositiveButton("Confirm", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            viewModel.onDeleteAccountBtnClicked(userId!!)
                            editor.remove("rememberMeChecked")
                            editor.remove(USER_ID)
                            editor.remove(USER_NAME)
                            editor.remove(USER_EMAIL)
                            editor.apply()
                            requireActivity().finish()
                        }
                    })
                    .setNegativeButton("Cancel") { p0, p1 -> p0?.dismiss() }.show()
        }

        binding.logOutBtn.setOnClickListener {
            editor.remove(REMEMBER_ME)
            editor.remove(USER_ID)
            editor.remove(USER_NAME)
            editor.remove(USER_EMAIL)
            editor.apply()

            loginViewModel.refuseAuthentication()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.authenticationState.observe(viewLifecycleOwner, Observer {
            if (it == LoginViewModel.AuthenticationState.UNAUTHENTICATED) {
                findNavController().popBackStack()
                findNavController().navigate(R.id.loginFragment)
            }
        })

        viewModel.toast.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        })
    }
}
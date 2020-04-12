package com.example.projectapp.ui.account.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.projectapp.R
import com.example.projectapp.databinding.FragmentRegisterBinding
import com.example.projectapp.network.getNetworkService
import com.example.projectapp.repository.UserRepository
import java.util.*

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels(
            factoryProducer = {
                RegisterViewModel.FACTORY(UserRepository(getNetworkService()))
            }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.createNewAccountBtn.setOnClickListener { view: View ->
            val name = binding.name.text.toString()
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
                viewModel.onCreateNewAccountBtnClicked(name, email, password)
            }
        }

        viewModel.user.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                Toast.makeText(context, it.name, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "false", Toast.LENGTH_LONG).show()
            }
        })
        return binding.root
    }
}
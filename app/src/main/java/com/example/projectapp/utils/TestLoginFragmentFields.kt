package com.example.projectapp.utils

import android.view.KeyEvent
import android.view.View
import com.example.projectapp.databinding.FragmentLoginBinding
import java.util.concurrent.atomic.AtomicBoolean

object TestLoginFragmentFields {

    fun testEmailField(binding: FragmentLoginBinding): Boolean {
        val allDone = AtomicBoolean(false)
        binding.email.setOnKeyListener { view: View?, i: Int, keyEvent: KeyEvent? ->
            if (binding.emailInputLayout.error == null) {
                binding.emailInputLayout.error = null
                if (binding.emailInputLayout.editText!!.text.toString().length > 20) {
                    binding.emailInputLayout.error = "Limited exceed !"
                    allDone.set(false)
                } else {
                    binding.emailInputLayout.error = null
                    allDone.set(true)
                }
                binding.executePendingBindings()
            } else if (binding.emailInputLayout.editText!!.text.toString().length <= 20) {
                binding.emailInputLayout.error = null
                allDone.set(true)
            }
            false
        }
        return allDone.get()
    }

    fun testPasswordField(binding: FragmentLoginBinding): Boolean {
        val allDone = AtomicBoolean(false)
        binding.password.setOnKeyListener { view: View?, i: Int, keyEvent: KeyEvent? ->
            if (binding.passwordInputLayout.error == null) {
                binding.passwordInputLayout.error = null
                if (binding.passwordInputLayout.editText!!.text.toString().length > 20) {
                    binding.passwordInputLayout.error = "Limited exceed !"
                    allDone.set(false)
                } else {
                    binding.passwordInputLayout.error = null
                    allDone.set(true)
                }
                binding.executePendingBindings()
            } else if (binding.passwordInputLayout.editText!!.text.toString().length <= 20) {
                binding.passwordInputLayout.error = null
                allDone.set(true)
            }
            false
        }
        return allDone.get()
    }
}
package com.example.projectapp.utils;

import com.example.projectapp.databinding.FragmentLoginBinding;

import java.util.concurrent.atomic.AtomicBoolean;

public class TestLoginFragmentFields {

    public static boolean testEmailField(FragmentLoginBinding binding) {

        AtomicBoolean allDone = new AtomicBoolean(false);

        binding.email.setOnKeyListener((view, i, keyEvent) -> {

            if (binding.emailInputLayout.getError() == null) {

                binding.emailInputLayout.setError(null);
                if (binding.emailInputLayout.getEditText().getText().toString().length() > 20) {
                    binding.emailInputLayout.setError("Limited exceed !");
                    allDone.set(false);
                } else {
                    binding.emailInputLayout.setError(null);
                    allDone.set(true);
                }
                binding.executePendingBindings();
            } else if (binding.emailInputLayout.getEditText().getText().toString().length() <= 20) {
                binding.emailInputLayout.setError(null);
                allDone.set(true);

            }
            return false;
        });
        return allDone.get();
    }

    public static boolean testPasswordField(FragmentLoginBinding binding) {

        AtomicBoolean allDone = new AtomicBoolean(false);

        binding.password.setOnKeyListener((view, i, keyEvent) -> {
            if (binding.passwordInputLayout.getError() == null) {
                binding.passwordInputLayout.setError(null);
                if (binding.passwordInputLayout.getEditText().getText().toString().length() > 20) {
                    binding.passwordInputLayout.setError("Limited exceed !");
                    allDone.set(false);
                } else {
                    binding.passwordInputLayout.setError(null);
                    allDone.set(true);
                }
                binding.executePendingBindings();
            } else if (binding.passwordInputLayout.getEditText().getText().toString().length() <= 20) {
                binding.passwordInputLayout.setError(null);
                allDone.set(true);
            }
            return false;
        });
        return allDone.get();
    }
}

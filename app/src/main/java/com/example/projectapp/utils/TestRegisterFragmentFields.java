package com.example.projectapp.utils;

import com.example.projectapp.databinding.FragmentPersonalDataBinding;
import com.example.projectapp.databinding.FragmentRegisterBinding;

import java.util.concurrent.atomic.AtomicBoolean;

public class TestRegisterFragmentFields {
/*
    public static boolean testCompanyNameField(FragmentCompanyDataBinding binding) {

        AtomicBoolean allDone = new AtomicBoolean(false);

        binding.companyName.setOnKeyListener((view, i, keyEvent) -> {

            if (binding.companyNameInputLayout.getError() == null) {

                binding.companyNameInputLayout.setError(null);
                if (binding.companyNameInputLayout.getEditText().getText().toString().length() > 100) {
                    binding.companyNameInputLayout.setError("Limited exceed !");
                    allDone.set(false);
                } else {
                    binding.companyNameInputLayout.setError(null);
                    allDone.set(true);
                }
                binding.executePendingBindings();
            } else if (binding.companyNameInputLayout.getEditText().getText().toString().length() <= 100) {
                binding.companyNameInputLayout.setError(null);
                allDone.set(true);
            }
            return false;
        });
        return allDone.get();
    }

    public static boolean testCompanyLocationField(FragmentCompanyDataBinding binding) {

        AtomicBoolean allDone = new AtomicBoolean(false);

        binding.companyLocation.setOnKeyListener((view, i, keyEvent) -> {

            if (binding.copmanyLocationInputLayout.getError() == null) {

                binding.copmanyLocationInputLayout.setError(null);
                if (binding.copmanyLocationInputLayout.getEditText().getText().toString().length() > 100) {
                    binding.copmanyLocationInputLayout.setError("Limited exceed !");
                    allDone.set(false);
                } else {
                    binding.copmanyLocationInputLayout.setError(null);
                    allDone.set(true);
                }
                binding.executePendingBindings();
            } else if (binding.copmanyLocationInputLayout.getEditText().getText().toString().length() <= 100) {
                binding.copmanyLocationInputLayout.setError(null);
                allDone.set(true);
            }
            return false;
        });
        return allDone.get();
    }

    public static boolean testCompanyDescField(FragmentCompanyDataBinding binding) {

        AtomicBoolean allDone = new AtomicBoolean(false);

        binding.companyDesc.setOnKeyListener((view, i, keyEvent) -> {

            if (binding.copmanyDescInputLayout.getError() == null) {

                binding.copmanyDescInputLayout.setError(null);
                if (binding.copmanyDescInputLayout.getEditText().getText().toString().length() > 200) {
                    binding.copmanyDescInputLayout.setError("Limited exceed !");
                    allDone.set(false);
                } else {
                    binding.copmanyDescInputLayout.setError(null);
                    allDone.set(true);
                }
                binding.executePendingBindings();
            } else if (binding.copmanyDescInputLayout.getEditText().getText().toString().length() <= 200) {
                binding.copmanyDescInputLayout.setError(null);
                allDone.set(true);
            }
            return false;
        });
        return allDone.get();
    }


    public static boolean testFirstNameField(FragmentPersonalDataBinding binding) {

        AtomicBoolean allDone = new AtomicBoolean(false);

        binding.firstName.setOnKeyListener((view, i, keyEvent) -> {

            if (binding.firstNameInputLayout.getError() == null) {

                binding.firstNameInputLayout.setError(null);
                if (binding.firstNameInputLayout.getEditText().getText().toString().length() > 10) {
                    binding.firstNameInputLayout.setError("Limited exceed !");
                    allDone.set(false);
                } else {
                    binding.firstNameInputLayout.setError(null);
                    allDone.set(true);
                }
                binding.executePendingBindings();
            } else if (binding.firstNameInputLayout.getEditText().getText().toString().length() <= 10) {
                binding.firstNameInputLayout.setError(null);
                allDone.set(true);
            }
            return false;
        });
        return allDone.get();
    }

    public static boolean testLastNameField(FragmentPersonalDataBinding binding) {

        AtomicBoolean allDone = new AtomicBoolean(false);

        binding.lastName.setOnKeyListener((view, i, keyEvent) -> {

            if (binding.lastNameInputLayout.getError() == null) {

                binding.lastNameInputLayout.setError(null);
                if (binding.lastNameInputLayout.getEditText().getText().toString().length() > 10) {
                    binding.lastNameInputLayout.setError("Limited exceed !");
                    allDone.set(false);
                } else {
                    binding.lastNameInputLayout.setError(null);
                    allDone.set(true);
                }
                binding.executePendingBindings();
            } else if (binding.lastNameInputLayout.getEditText().getText().toString().length() <= 10) {
                binding.lastNameInputLayout.setError(null);
                allDone.set(true);
            }
            return false;
        });
        return allDone.get();
    }
*/
    public static boolean testEmailField(FragmentPersonalDataBinding binding) {
// todo check email address
return false;
    }

    public static boolean testPhoneField(FragmentPersonalDataBinding binding) {

        AtomicBoolean allDone = new AtomicBoolean(false);

        binding.phone.setOnKeyListener((view, i, keyEvent) -> {

            if (binding.phoneInputLayout.getError() == null) {

                binding.phoneInputLayout.setError(null);
                if (binding.phoneInputLayout.getEditText().getText().toString().length() > 10) {
                    binding.phoneInputLayout.setError("Limited exceed !");
                    allDone.set(false);
                } else {
                    binding.phoneInputLayout.setError(null);
                    allDone.set(true);
                }
                binding.executePendingBindings();
            } else if (binding.phoneInputLayout.getEditText().getText().toString().length() <= 10) {
                binding.phoneInputLayout.setError(null);
                allDone.set(true);
            }
            return false;
        });
        return allDone.get();
    }


}

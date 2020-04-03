package com.example.projectapp.ui.account.register.data;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectapp.R;
import com.example.projectapp.databinding.FragmentPersonalDataBinding;
import com.example.projectapp.utils.TestRegisterFragmentFields;

public class DataFragment extends Fragment {
    private FragmentPersonalDataBinding binding;

    public DataFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_data, container, false);

       // TestRegisterFragmentFields.testFirstNameField(binding);
       // TestRegisterFragmentFields.testLastNameField(binding);
        TestRegisterFragmentFields.testEmailField(binding);
        TestRegisterFragmentFields.testPhoneField(binding);

        return binding.getRoot();
    }

}

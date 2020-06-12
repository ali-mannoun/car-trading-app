package com.example.projectapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.projectapp.R

class ProfileFragment : Fragment() {
    private var toolsViewModel: ProfileViewModel? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        toolsViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        //        final TextView textView = root.findViewById(R.id.text_tools);
        /* toolsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText("profile");
            }
        });*/return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}
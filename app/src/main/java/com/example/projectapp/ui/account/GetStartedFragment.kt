package com.example.projectapp.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.projectapp.R
import com.example.projectapp.databinding.FragmentGetStartedBinding

class GetStartedFragment : Fragment() {
    private lateinit var binding: FragmentGetStartedBinding
    private val viewModel: GetStartedViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_started, container, false)

        binding.getStartedBtn.setOnClickListener { view: View? ->
            view?.findNavController()?.navigate(GetStartedFragmentDirections.actionGetStartedFragmentToLoginFragment())
        }

        return binding.root
    }
}
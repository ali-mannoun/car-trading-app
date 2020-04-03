package com.example.projectapp.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.projectapp.R
import com.example.projectapp.databinding.FragmentGetStartedBinding
import com.example.projectapp.network.AccountApi
import com.example.projectapp.network.IApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetStartedFragment : Fragment() {
    private lateinit var binding: FragmentGetStartedBinding
    private lateinit var viewModel: GetStartedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_started, container, false)
        viewModel = ViewModelProvider(this).get(GetStartedViewModel::class.java)

        binding.getStartedBtn.setOnClickListener { view: View? ->
            Navigation.findNavController(view!!)
                    .navigate(GetStartedFragmentDirections.actionGetStartedFragmentToLoginFragment())
        }

        return binding.root
    }
}
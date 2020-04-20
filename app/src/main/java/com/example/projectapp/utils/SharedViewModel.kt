package com.example.projectapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val onBottomNavigationViewVisible = MutableLiveData<Boolean>()

    fun setBottomNavigationViewVisibility(value: Boolean) {
        onBottomNavigationViewVisible.value = value
    }

    val onBottomNavigationViewVisibilitySelected: LiveData<Boolean>
        get() = onBottomNavigationViewVisible
}
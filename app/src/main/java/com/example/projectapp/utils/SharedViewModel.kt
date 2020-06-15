package com.example.projectapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val onBottomNavigationViewVisible = MutableLiveData<Boolean>()
    val onBottomNavigationViewVisibilitySelected: LiveData<Boolean>
        get() = onBottomNavigationViewVisible

    //To control the toolbar visibility when showing the GetStartedFragment
    private val onIntroLayoutStarted = MutableLiveData<Boolean>()
    val onStartIntroLayout: LiveData<Boolean>
        get() = onIntroLayoutStarted

    fun setBottomNavigationViewVisibility(isVisible: Boolean) {
        onBottomNavigationViewVisible.value = isVisible
    }

    fun setActiveIntroStarted(isVisible: Boolean) {
        onIntroLayoutStarted.value = isVisible
    }
}
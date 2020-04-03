package com.example.projectapp.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<String> onNavigationFromBottomSheetToRegister = new MutableLiveData<>();
    private final MutableLiveData<Integer> onNavigationFromEmployeeDialog = new MutableLiveData<>();

    public void select(String item) {
        onNavigationFromBottomSheetToRegister.setValue(item);
    }

    public LiveData<String> getSelected() {
        return onNavigationFromBottomSheetToRegister;
    }

    public void navigationComplete() {
        onNavigationFromBottomSheetToRegister.setValue(null);
    }

    public void onDialogPositiveClick(int item) {
        onNavigationFromEmployeeDialog.setValue(item);
    }

    public LiveData<Integer> getDialogPositiveClicked() {
        return onNavigationFromEmployeeDialog;
    }

    public void dialogActionComplete() {
        onNavigationFromEmployeeDialog.setValue(null);
    }


}

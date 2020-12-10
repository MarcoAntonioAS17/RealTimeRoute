package com.example.project_test.ui.realTimeRoutes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RTRViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RTRViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Real Time Routes fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
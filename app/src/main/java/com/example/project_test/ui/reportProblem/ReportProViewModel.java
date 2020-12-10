package com.example.project_test.ui.reportProblem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReportProViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReportProViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("This is Report Problem fragment");
    }

    public LiveData<String> getText(){ return mText;}

}

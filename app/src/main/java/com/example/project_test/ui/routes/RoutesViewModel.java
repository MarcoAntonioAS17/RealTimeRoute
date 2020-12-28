package com.example.project_test.ui.routes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Set;

public class RoutesViewModel extends ViewModel {

    private MutableLiveData<String> Ruta;

    public RoutesViewModel() {
        Ruta = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return Ruta;
    }



    public void setText(MutableLiveData<String> ruta) {
        this.Ruta = ruta;
    }
}
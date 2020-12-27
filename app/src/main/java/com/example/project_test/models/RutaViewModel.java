package com.example.project_test.models;

import android.net.Uri;

public class RutaViewModel {

    private String Nombre;
    private String Photo;

    public RutaViewModel(String Nombre, String img){
        this.Nombre = Nombre;
        this.Photo = img;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getImg() {
        return Photo;
    }

    public void setImg(String img) {
        Photo = img;
    }

}

package com.example.project_test.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetallesRutaObject {

    private String Nombre;
    private String Horario;
    private String Photo;
    private List<GeoPoint> Ruta_Ida;
    private List<GeoPoint> Ruta_Vuelta;

    public DetallesRutaObject() {
    }

    public DetallesRutaObject(String Nombre, String Horario, String Photo, List<GeoPoint> Ruta_Ida, List<GeoPoint> Ruta_Vuelta) {
        this.Nombre = Nombre;
        this.Horario = Horario;
        this.Photo = Photo;
        this.Ruta_Ida = Ruta_Ida;
        this.Ruta_Vuelta = Ruta_Vuelta;

    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getHorario() {
        return Horario;
    }

    public void setHorario(String horario) {
        Horario = horario;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public List<GeoPoint> getRuta_Ida() {
        return Ruta_Ida;
    }

    public void setRuta_Ida(List<GeoPoint> ruta_Ida) {
        Ruta_Ida = ruta_Ida;
    }

    public List<GeoPoint> getRuta_Vuelta() {
        return Ruta_Vuelta;
    }

    public void setRuta_Vuelta(List<GeoPoint> ruta_Vuelta) {
        Ruta_Vuelta = ruta_Vuelta;
    }
}
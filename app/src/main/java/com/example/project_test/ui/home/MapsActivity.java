package com.example.project_test.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.project_test.MainActivity;
import com.example.project_test.R;
import com.example.project_test.models.DetallesRutaObject;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MapsActivity extends Fragment
        implements
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener{

    private GoogleMap mMap;
    private MapView mapView;
    DocumentReference docRef;
    View mView;
    private FirebaseStorage storage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mView = inflater.inflate(R.layout.activity_maps, container, false);
        return  mView;
    }

    @Override
    public void onViewCreated(@NonNull View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) mView.findViewById(R.id.map);
        if(mapView !=null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).showFloatingActionButton();
        //Optenemos y cargamos datos

        //obtener_datos();

        /*String cadena =  lista_json.toString();
        String filename = "Rutas.json";
        Log.d("TYAM","Guardando datos");

        try{
            File root = new File(getContext().getFilesDir(), "Rutas");
            if(!root.exists()){
                root.mkdirs();
            }
            File archivo = new File(root,filename);
            FileWriter fileWriter = new FileWriter(archivo);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(cadena);
            bufferedWriter.close();
            fileWriter.close();
            Toast.makeText(getContext(),"Datos guardados =>"+ cadena.getBytes().length + "Bytes",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }*/


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Importante!! Es para la carga de datos desde Firestore
        //db = FirebaseFirestore.getInstance();
        //docRef = db.collection("Rutas").document("Seguro_Tecnologico");

        //poner_route_ida(mMap);
        //poner_route_vuelta(mMap);

        mMap.addMarker(new MarkerOptions().
                position(new LatLng(19.950998597968574, -96.84478038961053))
                .title("Fin"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(19.91954293785951, -96.8667806490291))
                .title("Inicio")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        LatLngBounds ruta = new LatLngBounds(
                new LatLng(19.91954293785951, -96.8667806490291),
                new LatLng(19.950998597968574, -96.84478038961053)
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(ruta,500,500,10));

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    private void poner_route(GoogleMap googleMap) {

        ArrayList<LatLng> puntos_ida = new ArrayList<>();
        ArrayList<LatLng> puntos_vuelta = new ArrayList<>();

        docRef
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DetallesRutaObject ruta = documentSnapshot.toObject(DetallesRutaObject.class);
                        Log.d("TYAM",ruta.getNombre() + " IDA");
                        Log.d("TYAM",ruta.getRuta_Ida().toString());

                        for( GeoPoint punto: ruta.getRuta_Ida()){
                            double Lan = punto.getLatitude();
                            double Lng = punto.getLongitude();
                            LatLng latLng = new LatLng(Lan,Lng);
                            Log.d("TYAM",punto.getLatitude()+","+punto.getLongitude());
                            puntos_ida.add(latLng);
                        }
                        poner_flechas(googleMap,puntos_ida);

                        googleMap.addPolyline(new PolylineOptions()
                                .clickable(true)
                                .color(Color.RED)
                                .addAll(puntos_ida));

                        for( GeoPoint punto: ruta.getRuta_Vuelta()){
                            double Lan = punto.getLatitude();
                            double Lng = punto.getLongitude();
                            LatLng latLng = new LatLng(Lan,Lng);
                            Log.d("TYAM",punto.getLatitude()+","+punto.getLongitude());
                            puntos_vuelta.add(latLng);
                        }

                        poner_flechas(googleMap,puntos_vuelta);

                        googleMap.addPolyline(new PolylineOptions()
                                .clickable(true)
                                .color(Color.GREEN)
                                .addAll(puntos_vuelta));
                    }

                });

    }

    private void poner_flechas(GoogleMap googleMap, ArrayList<LatLng> puntos){
        Double rotation = 0.0;
        for (int i=0; i < puntos.size()-1; i+=2){
            LatLng punto_1 = puntos.get(i);
            LatLng punto_2 = puntos.get(i+1);

            rotation = SphericalUtil.computeHeading(punto_1,punto_2);

            double LatInt = punto_1.latitude + punto_2.latitude;
            double LngInt = punto_1.longitude + punto_2.longitude;

            LatLng IntPoint = new LatLng(LatInt/2,LngInt/2);

            googleMap.addMarker(new MarkerOptions()
                    .position(IntPoint)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow))
                    .flat(true)
                    .anchor(0.5f, 0.5f)
                    .rotation(Float.parseFloat(rotation.toString()))

            );
        }
    }

    private void obtener_datos() {
        Snackbar snackbar = Snackbar.make (mapView, "Obteniendo información...", Snackbar.LENGTH_INDEFINITE);
        ViewGroup layer = (ViewGroup) snackbar.getView ().findViewById (com.google.android.material.R.id.snackbar_text).getParent ();
        ProgressBar bar = new ProgressBar (getContext ());
        layer.addView (bar);
        snackbar.show ();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        ArrayList<DetallesRutaObject> Data = new ArrayList<>();
        db.collection("Rutas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot elem: task.getResult()){
                                DetallesRutaObject druta = elem.toObject(DetallesRutaObject.class);
                                Log.d("TYAM",druta.getNombre() );
                                Data.add(druta);
                                Log.d("PhotoURL","=>"+druta.getPhoto());
                                DownloadImage(druta.getNombre(),druta.getPhoto());
                                druta.setPhoto(getContext().getFilesDir()+"/Fotos/"+druta.getNombre()+".jpg");
                                Log.d("PhotoFile","=>"+druta.getPhoto());
                            }
                            switch (save_data(Data)){
                                case 0:
                                    snackbar.dismiss ();
                                    Toast.makeText(getContext(),"Datos guardados ",Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    snackbar.dismiss ();
                                    Toast.makeText(getContext(),"Espacio insuficiente ",Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    snackbar.dismiss ();
                                    Toast.makeText(getContext(),"Error al guardar datos ",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(getContext(),"Error al guardar datos ",Toast.LENGTH_LONG).show();
                        }
                    }


                });

        /*
        ArrayList<LatLng> puntos_ida = new ArrayList<>();
        ArrayList<LatLng> puntos_vuelta = new ArrayList<>();

        docRef
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DetallesRutaObject ruta = documentSnapshot.toObject(DetallesRutaObject.class);
                        Log.d("TYAM",ruta.getNombre() + " IDA");
                        Log.d("TYAM",ruta.getRuta_Ida().toString());

                        for( GeoPoint punto: ruta.getRuta_Ida()){
                            double Lan = punto.getLatitude();
                            double Lng = punto.getLongitude();
                            LatLng latLng = new LatLng(Lan,Lng);
                            Log.d("TYAM",punto.getLatitude()+","+punto.getLongitude());
                            puntos_ida.add(latLng);
                        }

                        for( GeoPoint punto: ruta.getRuta_Vuelta()){
                            double Lan = punto.getLatitude();
                            double Lng = punto.getLongitude();
                            LatLng latLng = new LatLng(Lan,Lng);
                            Log.d("TYAM",punto.getLatitude()+","+punto.getLongitude());
                            puntos_vuelta.add(latLng);
                        }


                    }

                });
        */
    }

    public void DownloadImage(String fileName,String url) {
        StorageReference photoBusRef = storage.getReferenceFromUrl(url);
        File root = new File(getContext().getFilesDir(), "Fotos");
        if(!root.exists())
            root.mkdirs();
        File archivo = new File(root,fileName+".jpg");
        photoBusRef.getFile(archivo).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d("PHOTO","=>Archivo creado "+archivo.getAbsolutePath());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

    }

    public int save_data(List<DetallesRutaObject> list){
        List<JSONObject> lista_json = new ArrayList<>();

        File root = new File(getContext().getFilesDir(), "Rutas");
        if(!root.exists())
            root.mkdirs();

        for( DetallesRutaObject elem: list) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Nombre", elem.getNombre());
                jsonObject.put("Horario", elem.getHorario());
                jsonObject.put("Foto", elem.getPhoto());
                jsonObject.put("Ruta_Ida", elem.getRuta_Ida());
                jsonObject.put("Ruta_Vuelta", elem.getRuta_Vuelta());
                lista_json.add(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                return 2;
            }
        }
        long espacio_requerido = lista_json.toString().getBytes().length;
        //Comprobamos que halla espacio suficiente
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            StorageManager storageManager = getContext().getSystemService(StorageManager.class);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                try {
                    UUID appSepecificInternalDirUuid = storageManager.getUuidForPath(root);
                    long availableBytes =
                            storageManager.getAllocatableBytes(appSepecificInternalDirUuid);
                    if(availableBytes >= espacio_requerido) {
                        storageManager.allocateBytes(
                                appSepecificInternalDirUuid, espacio_requerido);
                    }else{
                        return 1;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return 2;
                }
            }
        }

        for(JSONObject jsonObject: lista_json){
            try {
                File archivo = new File(root,jsonObject.getString("Nombre")+".json");
                FileWriter fileWriter = new FileWriter(archivo);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(jsonObject.toString());
                bufferedWriter.close();
                fileWriter.close();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return 2;
            }

        }
        return 0;

    }
}
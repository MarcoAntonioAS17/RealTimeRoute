package com.example.project_test.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.SphericalUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MapsActivity extends Fragment
        implements
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener{

    private GoogleMap mMap;
    private MapView mapView;
    FirebaseFirestore db;
    DocumentReference docRef;
    View mView;
    private static final String FILE_NAME = "Rutas";

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

        //Optenemos y cargamos datos
        List<DetallesRutaObject> list = objetos_rutas();
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
                        Toast.makeText(getContext(),"Espacio insuficiente ",Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
            }

        }
        Toast.makeText(getContext(),"Datos guardados ",Toast.LENGTH_SHORT).show();
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

    private List<DetallesRutaObject> objetos_rutas(){
        ArrayList<DetallesRutaObject> Rutas = new ArrayList<>();

        List<GeoPoint> Ruta_Ida = new ArrayList<>();
        {
            Ruta_Ida.add(new GeoPoint(19.91954293785951, -96.8667806490291));
            Ruta_Ida.add(new GeoPoint(19.91973120965887, -96.86620194388682));
            Ruta_Ida.add(new GeoPoint(19.920155378699953, -96.86530608694218));
            Ruta_Ida.add(new GeoPoint(19.920312244394616, -96.86466093902305));
            Ruta_Ida.add(new GeoPoint(19.920382833906444, -96.86381279197722));
            Ruta_Ida.add(new GeoPoint(19.920547542645192, -96.86344016343183));
            Ruta_Ida.add(new GeoPoint(19.920866501927378, -96.86308143894452));
            Ruta_Ida.add(new GeoPoint(19.921261259522634, -96.86273479160943));
            Ruta_Ida.add(new GeoPoint(19.922097470974837, -96.86244005698806));
            Ruta_Ida.add(new GeoPoint(19.922259563527533, -96.86225652352942));
            Ruta_Ida.add(new GeoPoint(19.921254287066972, -96.8592136207038));
            //seguro
            Ruta_Ida.add(new GeoPoint(19.922839198330244, -96.85860929512927));
            Ruta_Ida.add(new GeoPoint(19.9244808977876, -96.85803744258286));
            //Cabeza Benito
            Ruta_Ida.add(new GeoPoint(19.924619787483305, -96.85800699386904));
            Ruta_Ida.add(new GeoPoint(19.924666437430066, -96.85801601571016));
            Ruta_Ida.add(new GeoPoint(19.92473323165029, -96.85796188466313));
            //Justa Garcia Esq Venustian Carranza
            Ruta_Ida.add(new GeoPoint(19.92382031865323, -96.85555965436501));
            //Venustiano Carranza Esq Xalapa
            Ruta_Ida.add(new GeoPoint(19.923459424514558, -96.85454510215185));
        }
        List<GeoPoint> Ruta_Regreso = new ArrayList<>();
        {
            Ruta_Regreso.add(new GeoPoint(19.950998597968574, -96.84478038961053));
            Ruta_Regreso.add(new GeoPoint(19.950834087258958, -96.84513310009034));
            //Entrada Tec
            Ruta_Regreso.add(new GeoPoint(19.950400177331225, -96.84601440926227));
            Ruta_Regreso.add(new GeoPoint(19.950400177331225, -96.84601440926227));
            Ruta_Regreso.add(new GeoPoint(19.950250160339753, -96.84624582121279));
            Ruta_Regreso.add(new GeoPoint(19.94940256168473, -96.84722333716583));
            //Gabriela Monte
            Ruta_Regreso.add(new GeoPoint(19.94784866420303, -96.84899794876381));
            //Gabriela Esq Super GiOS
            Ruta_Regreso.add(new GeoPoint(19.94669835353429, -96.85036216750035));
            //Gabriela Esq Carlos Carballal
            Ruta_Regreso.add(new GeoPoint(19.945967002219913, -96.85120402818755));
            //Gabriela Esq Miguel Hidalgo
            Ruta_Regreso.add(new GeoPoint(19.944793082530996, -96.85258850995484));
            //Gabriela Tecnica
            Ruta_Regreso.add(new GeoPoint(19.94381418436043, -96.8537296102019));
            //Gabriela Esq Isabela Catolica
            Ruta_Regreso.add(new GeoPoint(19.942701266511246, -96.85500330222682));
            //Gabriela Esq Manuel Avila
            Ruta_Regreso.add(new GeoPoint(19.941741106245125, -96.85395582368486));
            //Manuel Unidad
            Ruta_Regreso.add(new GeoPoint(19.94071858065521, -96.85279846461482));
            //Manuel Carriles
            Ruta_Regreso.add(new GeoPoint(19.93979421181931, -96.8517542308609));
            //Manuel Esq Enriques
            Ruta_Regreso.add(new GeoPoint(19.939102976729888, -96.85097975750448));
            //Manuel Esq Manuela
            Ruta_Regreso.add(new GeoPoint(19.938722791240192, -96.85064999960032));
            //Manuel
            Ruta_Regreso.add(new GeoPoint(19.93844345464908, -96.85045478502707));
            //Manel Laboratorio del Angel
            Ruta_Regreso.add(new GeoPoint(19.93822212203623, -96.85036364496867));
            //Manuel Esq de los Pocitos
            Ruta_Regreso.add(new GeoPoint(19.937975470601895, -96.85024215084623));
            //Manuel Esq Socrates
            Ruta_Regreso.add(new GeoPoint(19.93757909841644, -96.85017268279036));
            Ruta_Regreso.add(new GeoPoint(19.937241208559175, -96.85016826354203));
            //Manuel esq Carolino Anaya
            Ruta_Regreso.add(new GeoPoint(19.936808010132857, -96.85018414251844));
            Ruta_Regreso.add(new GeoPoint(19.936239202602117, -96.85032338427433));
            //Manuel Esq Unca Tejeda
            Ruta_Regreso.add(new GeoPoint(19.935151807938883, -96.85069986025093));
            //Manuel Ganadera
        }
        DetallesRutaObject detallesRutaObject = new DetallesRutaObject("Seguro-Tecnologico","6:00 - 22:00","",Ruta_Ida,Ruta_Regreso);
        Rutas.add(detallesRutaObject);

        List<GeoPoint> Ruta_Ida2 = new ArrayList<>();
        {
             Ruta_Ida2.add(new GeoPoint(19.91954293785951, -96.8667806490291));
             Ruta_Ida2.add(new GeoPoint(19.91973120965887, -96.86620194388682));
             Ruta_Ida2.add(new GeoPoint(19.920155378699953, -96.86530608694218));
             Ruta_Ida2.add(new GeoPoint(19.920312244394616, -96.86466093902305));
             Ruta_Ida2.add(new GeoPoint(19.920382833906444, -96.86381279197722));
             Ruta_Ida2.add(new GeoPoint(19.920547542645192, -96.86344016343183));
             Ruta_Ida2.add(new GeoPoint(19.920866501927378, -96.86308143894452));
             Ruta_Ida2.add(new GeoPoint(19.921261259522634, -96.86273479160943));
             Ruta_Ida2.add(new GeoPoint(19.922097470974837, -96.86244005698806));
             Ruta_Ida2.add(new GeoPoint(19.922259563527533, -96.86225652352942));
             Ruta_Ida2.add(new GeoPoint(19.921254287066972, -96.8592136207038 ));
            //seguro
             Ruta_Ida2.add(new GeoPoint(19.922839198330244, -96.85860929512927));
             Ruta_Ida2.add(new GeoPoint(19.9244808977876, -96.85803744258286));
            //Cabeza Benito
             Ruta_Ida2.add(new GeoPoint(19.924619787483305, -96.85800699386904));
             Ruta_Ida2.add(new GeoPoint(19.924666437430066, -96.85801601571016));
             Ruta_Ida2.add(new GeoPoint(19.92473323165029, -96.85796188466313));
            //Justa Garcia Esq Venustian Carranza
             Ruta_Ida2.add(new GeoPoint(19.92382031865323, -96.85555965436501));
            //Venustiano Carranza Esq Xalapa
             Ruta_Ida2.add(new GeoPoint(19.923459424514558, -96.85454510215185));
            //Carranza Esq Comonfort
             Ruta_Ida2.add(new GeoPoint(19.925280474584643, -96.85457778842867));

        }

        List<GeoPoint> Ruta_Regreso2 = new ArrayList<>();
        {
             Ruta_Regreso2.add(new GeoPoint(19.92513162670305, -96.84143550638939));
             Ruta_Regreso2.add(new GeoPoint(19.925310466540452, -96.8415006526834));
             Ruta_Regreso2.add(new GeoPoint(19.926041482239416, -96.84210194310234));
             Ruta_Regreso2.add(new GeoPoint(19.927121997393208, -96.84297833038929));
             Ruta_Regreso2.add(new GeoPoint(19.927618310937223, -96.84341539293608));
             Ruta_Regreso2.add(new GeoPoint(19.927713854032604, -96.84338151686346));
             Ruta_Regreso2.add(new GeoPoint(19.927890241128736, -96.84328770620009));
             Ruta_Regreso2.add(new GeoPoint(19.928754849627442, -96.8421940757961));
             Ruta_Regreso2.add(new GeoPoint(19.929501672176904, -96.84125538621441));
             Ruta_Regreso2.add(new GeoPoint(19.929724207032656, -96.84132627292881));
        }

        DetallesRutaObject detallesRutaObject1 = new DetallesRutaObject("Seguro-Palchan","6:00 - 22:00","",Ruta_Ida2,Ruta_Regreso2);
        Rutas.add(detallesRutaObject1);

        return Rutas;

    }

    private void poner_route_ida(GoogleMap googleMap) {

        ArrayList<LatLng> puntos = new ArrayList<>();

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
                            puntos.add(latLng);
                        }
                        poner_flechas(googleMap,puntos);

                        googleMap.addPolyline(new PolylineOptions()
                                .clickable(true)
                                .color(Color.RED)
                                .addAll(puntos));
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

    private void poner_route_vuelta(GoogleMap googleMap) {

        ArrayList<LatLng> puntos = new ArrayList<>();

        docRef
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DetallesRutaObject ruta = documentSnapshot.toObject(DetallesRutaObject.class);
                        Log.d("TYAM",ruta.getNombre() + " Vuelta");
                        Log.d("TYAM",ruta.getRuta_Vuelta().toString());

                        for( GeoPoint punto: ruta.getRuta_Vuelta()){
                            double Lan = punto.getLatitude();
                            double Lng = punto.getLongitude();
                            LatLng latLng = new LatLng(Lan,Lng);
                            Log.d("TYAM",punto.getLatitude()+","+punto.getLongitude());
                            puntos.add(latLng);
                        }
                        poner_flechas(googleMap,puntos);

                        googleMap.addPolyline(new PolylineOptions()
                                .clickable(true)
                                .color(Color.GREEN)
                                .addAll(puntos));
                    }

                });

    }
}
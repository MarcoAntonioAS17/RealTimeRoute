package com.example.project_test.ui.realTimeRoutes;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.project_test.MainActivity;
import com.example.project_test.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RtrMapaRutas extends Fragment
        implements
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener {


    private GoogleMap mMap;
    private MapView mapView;
    View mView;
    private static final String RUTA_ID = "Ruta";
    List<LatLng> latLngIda;
    List<LatLng> latLngVuelta;
    boolean activar_ida = true, activar_regreso = true;

    FloatingActionButton fab_gps;
    LocationManager locationManager = null;
    LocationListener locationListener = null;
    double latitude = 0, longitude = 0;
    boolean gps_activado = false;

    FirebaseDatabase database;
    DatabaseReference myRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_mapa_rtr_rutas, container, false);

        mView.setLabelFor(R.id.mapa_rtr_rutas);

        ExtendedFloatingActionButton fab_ida = mView.findViewById(R.id.fab_ruta_ida);
        fab_ida.setOnClickListener(view ->{
                    activar_ida = !activar_ida;
                    cargar_elementos(mMap);

                    if (activar_ida == false)
                        fab_ida.setBackgroundColor(Color.LTGRAY);
                    else
                        fab_ida.setBackgroundColor(getResources().getColor(R.color.orange));
                }
        );

        ExtendedFloatingActionButton fab_regreso = mView.findViewById(R.id.fab_ruta_regreso);
        fab_regreso.setOnClickListener(view ->{
                    activar_regreso = !activar_regreso;
                    cargar_elementos(mMap);
                    if (activar_regreso == false)
                        fab_regreso.setBackgroundColor(Color.LTGRAY);
                    else
                        fab_regreso.setBackgroundColor(getResources().getColor(R.color.orange));
                }
        );

        fab_gps = mView.findViewById(R.id.fab_gps_rt);
        fab_gps.setOnClickListener(view ->{
                    gps_activado = !gps_activado;
                    if (gps_activado){
                        boton_gps();

                        Toast.makeText(getContext(),"GPS activado",Toast.LENGTH_LONG).show();
                    }else{
                        locationManager.removeUpdates(locationListener);
                        Toast.makeText(getContext(),"GPS desactivado",Toast.LENGTH_LONG).show();
                        myRef.removeValue();
                    }
                }
        );

        locationListener = (location) -> {
            latitude = location.getLatitude ();
            longitude = location.getLongitude ();
            Log.d ("TYAM", "Latitude " + latitude + " - Longitude " + longitude);

            cargar_elementos(mMap);
            subir_datos(latitude,longitude);
        };

        return  mView;
    }

    private void subir_datos(double lat, double lng) {

        LatLng latLng = new LatLng(lat,lng);

        myRef.setValue(latLng);


    }


    private void boton_gps() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);


            return;
        }

        locationManager = (LocationManager) getContext().getSystemService (Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled (LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates (LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myRef.removeValue();
        try{
            locationManager.removeUpdates(locationListener);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mapView = (MapView) mView.findViewById(R.id.map);
        if(mapView !=null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
        Log.w("TYAM","Mapa rutas onViewCreated");
        Bundle bundle = getArguments ();
        if (bundle == null) return;
        String ruta =  bundle.getString(RUTA_ID);
        String numero = "Camion "+bundle.getString("Texto");
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("RTR: "+ruta);

        readFile(ruta);
        database= FirebaseDatabase.getInstance();
        myRef = database.getReference(ruta).child(numero);
    }


    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        cargar_elementos(googleMap);

        LatLngBounds ruta = new LatLngBounds(
                latLngIda.get(0), latLngVuelta.get(0)
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(ruta,1000,1000,10));
    }

    private void cargar_elementos(GoogleMap googleMap) {

        mMap.clear();

        Polyline ida = null, regreso = null;

        if (latitude != 0){
            mMap.addMarker(new MarkerOptions().
                    position(new LatLng(latitude,longitude))
                    .title("Tú")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mi_bus)));
        }

        if (latLngIda != null && activar_ida ){
            poner_flechas(googleMap,latLngIda);

            ida = googleMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .color(Color.GREEN)
                    .addAll(latLngIda));
        }

        if (latLngVuelta !=null && activar_regreso){
            poner_flechas(googleMap,latLngVuelta);

            regreso = googleMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .color(Color.RED)
                    .addAll(latLngVuelta));
        }

        mMap.addMarker(new MarkerOptions().
                position(latLngVuelta.get(0))
                .title("Fin"));

        mMap.addMarker(new MarkerOptions()
                .position(latLngIda.get(0))
                .title("Inicio")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

    }

    private void readFile(String filename) {

        File file = new File(getContext().getFilesDir() + "/Rutas/" + filename + ".json");
        latLngIda = new ArrayList<>();
        latLngVuelta = new ArrayList<>();

        if (file.exists()) {

            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append("\n");
                }
                br.close();
            } catch (IOException e) {
            }
            try {
                JSONObject obj = new JSONObject(text.toString());
                String puntos = obj.getString("Ruta_Ida")
                        .replace("}]", "")
                        .replace("[", "}, ")
                        .replace("}", "")
                        .replace("{", "");
                String[] coorde = puntos.split(", GeoPoint  latitude=", -2);
                for (String elem : coorde) {
                    if (elem.length() > 5) {
                        String[] coord = elem.split(", longitude=");
                        LatLng latLng = new LatLng(Double.parseDouble(coord[0]), Double.parseDouble(coord[1]));
                        latLngIda.add(latLng);
                    }
                }
                puntos = obj.getString("Ruta_Vuelta")
                        .replace("}]", "")
                        .replace("[", "}, ")
                        .replace("}", "")
                        .replace("{", "");
                coorde = puntos.split(", GeoPoint  latitude=", -2);
                for (String elem : coorde) {
                    if (elem.length() > 5) {
                        String[] coord = elem.split(", longitude=");
                        LatLng latLng = new LatLng(Double.parseDouble(coord[0]), Double.parseDouble(coord[1]));
                        latLngVuelta.add(latLng);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void poner_flechas(GoogleMap googleMap, List<LatLng> puntos){
        Double rotation = 0.0;
        for (int i=0; i < puntos.size()-1; i+=3){
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
}

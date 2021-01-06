package com.example.project_test.ui.realTimeRoutes;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_mapa_rtr_rutas, container, false);
        Log.d("TYAM","onCreateView");
        mView.setLabelFor(R.id.mapa_rtr_rutas);
        return  mView;
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
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("RTR: "+ruta);
        Log.d("TYAM","Ruta=>"+ruta);
        readFile(ruta);

    }


    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(latLngIda != null ){
            poner_flechas(googleMap,latLngIda);

            googleMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .color(Color.GREEN)
                    .addAll(latLngIda));

        }
        if(latLngVuelta !=null){
            poner_flechas(googleMap,latLngVuelta);

            googleMap.addPolyline(new PolylineOptions()
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

        LatLngBounds ruta = new LatLngBounds(
                latLngIda.get(0), latLngVuelta.get(0)
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(ruta,1000,1000,10));
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

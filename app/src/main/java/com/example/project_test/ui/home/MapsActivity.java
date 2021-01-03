package com.example.project_test.ui.home;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
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
import com.example.project_test.models.RutaViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.io.BufferedReader;
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
        GoogleMap.OnPolylineClickListener {

    FloatingActionButton fab_gps;
    FloatingActionButton fab_go;
    private GoogleMap mMap;
    private MapView mapView;
    View mView;
    private FusedLocationProviderClient fusedLocationClient;
    LocationManager locationManager = null;
    LocationListener locationListener = null;
    double latitude = 0, longitude = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_maps, container, false);
        fab_gps = mView.findViewById(R.id.fab_gps);
        fab_gps.setOnClickListener(view ->
                boton_gps()
        );

        fab_go = mView.findViewById(R.id.fab_go);
        fab_go.setOnClickListener(view ->
                Snackbar.make(view, "GO Float Action Button", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
        );
        locationListener = (location) -> {
            latitude = location.getLatitude ();
            longitude = location.getLongitude ();
            Log.d ("TYAM", "Latitude " + latitude + " - Longitude " + longitude);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().
                    position(new LatLng(latitude,longitude))
                    .title("Tú"));
        };

        return mView;
    }


    private void boton_gps() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);


            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.w("TYAM", "Latitude=>" + location.getLatitude() + " Longitud=>" + location.getLongitude());
                        }
                    }
                });

        locationManager = (LocationManager) getContext().getSystemService (Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled (LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates (LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
        }

        /*Snackbar.make(mView, "GPS Float Action Button", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/
    }

    @Override
    public void onPause () {
        super.onPause ();
        try{
            locationManager.removeUpdates(locationListener);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

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


        if (latitude != 0){
            mMap.addMarker(new MarkerOptions().
                    position(new LatLng(latitude,longitude))
                    .title("Tú"));
        }
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

}
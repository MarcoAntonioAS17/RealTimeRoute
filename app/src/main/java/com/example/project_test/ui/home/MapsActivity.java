package com.example.project_test.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_test.R;
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
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends Fragment
        implements
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener{

    private GoogleMap mMap;
    private MapView mapView;
    View mView;

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

        List<LatLng> ruta_camion = get_route_pouints();

        Double rotation = 0.0;
        for (int i=0; i < ruta_camion.size()-1; i+=2){
            LatLng punto_1 = ruta_camion.get(i);
            LatLng punto_2 = ruta_camion.get(i+1);

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

        googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .color(Color.RED)
                .addAll(ruta_camion));

        //polyline1.setTag("Ruta Matacocuite");

        mMap.addMarker(new MarkerOptions().
                position(new LatLng(19.19461903427766, -96.13627391062812))
                .title("Fin"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(19.13237707836943, -96.22609232873876))
                .title("Inicio")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        LatLngBounds ruta = new LatLngBounds(
                new LatLng(19.13237707836943, -96.22609232873876),
                new LatLng(19.19461903427766, -96.13627391062812)
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(ruta,500,500,10));
    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    private ArrayList<LatLng> get_route_pouints(){

        ArrayList<LatLng> puntos = new ArrayList<>();

        puntos.add(new LatLng(19.19461903427766, -96.13627391062812));//Parada Parque Zamora
        puntos.add(new LatLng(19.193775642743024, -96.13531217988783)); //Inicio Diaz Miron
        puntos.add(new LatLng(19.18604589730039, -96.13530018711339)); //Diaz Miron Esq. Iturbide
        puntos.add(new LatLng(19.179069499939672, -96.13500803002144)); //Diaz Miron Esq. Xalapa
        puntos.add(new LatLng(19.170053490675798, -96.13408882641768)); //Diaz Miron, Casi Tecnica
        puntos.add(new LatLng(19.164362996643195, -96.13320183329016)); //Diaz Miron Esq Reino Magico
        puntos.add(new LatLng(19.16297212795448, -96.13170369922472));//Diaz Miron Esq La Fragua
        puntos.add(new LatLng(19.155398370317798, -96.13068735028367)); //Diaz Miron Esq IMSS
        puntos.add(new LatLng(19.153351452200273, -96.13004448909456)); //Diaz Miron CFE
        puntos.add(new LatLng(19.151776145280998, -96.13005166916092)); //Diaz Miron Pemex
        puntos.add(new LatLng(19.14833781261501, -96.12911048937377));//Diaz Miron Bajando Puente
        puntos.add(new LatLng(19.146226079176557, -96.12937105734713)); //Diaz Miron Puente
        puntos.add(new LatLng(19.145780987427475, -96.12964986058783)); //Diaz Miron Puente 2
        puntos.add(new LatLng(19.145556938699382, -96.12987643705988)); //Diaz Miron Puente 3
        puntos.add(new LatLng(19.145500298377094, -96.1310289362719));//Home Depot
        puntos.add(new LatLng(19.144682158220224, -96.13312075565096)); //Ejercito Caño
        puntos.add(new LatLng(19.144418921224247, -96.13520036427619)); //Ejercito Cadillac
        puntos.add(new LatLng(19.141703145678893, -96.13787798008612)); //Ejercito Gasolinera
        puntos.add(new LatLng(19.141299655214024, -96.13868290753818)); //Ejercito Bodega Aur
        puntos.add(new LatLng(19.13990294983544, -96.14894983932474));//Federal bajo puente
        puntos.add(new LatLng(19.139887034476008, -96.14979879949844)); //Federal frente a policia
        puntos.add(new LatLng(19.149954824962197, -96.17400103559876)); //Federal Puente JB Lobos
        puntos.add(new LatLng(19.164527765715363, -96.21274893244686)); //Federal Progreso
        puntos.add(new LatLng(19.163464272719448, -96.21325082462064)); //Calle 8 Esq Av N 1
        puntos.add(new LatLng(19.162220385624675, -96.21035639302839)); //Av N 1 Esq Tamesis
        puntos.add(new LatLng(19.160275676046247, -96.21125012188728)); //Tamesis Esq Av N4
        puntos.add(new LatLng(19.16158217584853, -96.21415905903427));//Calle 8 Esq Av N 4
        puntos.add(new LatLng(19.164570150736726, -96.2192867718859));//Av N 4 Esq Calle 3
        puntos.add(new LatLng(19.161985427471436, -96.22149646274005)); //Calle 3 Esq Av N 8
        puntos.add(new LatLng(19.16041727017686, -96.21766098897767));//Av N 8 Esq Blvb de los Lirios
        puntos.add(new LatLng(19.160127153666743, -96.21776846928007)); //Blvb Lirios P1
        puntos.add(new LatLng(19.15947943184025, -96.21826217701908));//Blvb Lirios P2
        puntos.add(new LatLng(19.158468980710808, -96.21807017956503)); //Blvb Lirios P3
        puntos.add(new LatLng(19.158027959454103, -96.21816493990697)); //Blvb Lirios P4
        puntos.add(new LatLng(19.157475399833096, -96.21850359821408)); //Blvb Lirios P5
        puntos.add(new LatLng(19.157180405197614, -96.218332793675));  //Blvb Lirios Esq San Geronimo
        puntos.add(new LatLng(19.156845643481766, -96.2172036198914));//San Geronimo Esq Jose Rivero
        puntos.add(new LatLng(19.155325593492588, -96.21770125820427)); //Jose Rivero Esq San Pedro
        puntos.add(new LatLng(19.15472455848812, -96.21555355324762));//San Pedro Esq San Mateo
        puntos.add(new LatLng(19.15090012323203, -96.21682543788457));//San Mateo Esq Valente Diaz
        puntos.add(new LatLng(19.150680648597397, -96.21735840716296)); //Valente Curva
        puntos.add(new LatLng(19.150164562158682, -96.21761543212308)); //Valente - Mata Cocuite
        puntos.add(new LatLng(19.146206096626056, -96.21683599622091)); //Valente - Mata Cocuite Bodega Cafe
        puntos.add(new LatLng(19.13164431160751, -96.21565790682253));//Valente - Matacocuite Curva Camiones
        puntos.add(new LatLng(19.131221572838292, -96.21542131309481)); //Taller Camiones
        puntos.add(new LatLng(19.13090630528217, -96.21438129016255));//Autolavado
        puntos.add(new LatLng(19.13074341680442, -96.21428674262054));//Autolavado Curva
        puntos.add(new LatLng(19.127487870009364, -96.21492565495231)); //Campo
        puntos.add(new LatLng(19.12803436222983, -96.21711058088087));//Entrada
        puntos.add(new LatLng(19.128008910821393, -96.2172991519837));//Entrada Y
        puntos.add(new LatLng(19.12782711493077, -96.21755314572003));//Av Gaviota Esq Calle Ruisenor
        puntos.add(new LatLng(19.127438501696144, -96.2197344933304));//Av Gaviota Esq Calle Mirlo
        puntos.add(new LatLng(19.127107505828413, -96.22227443585841)); //Av Gaviota Esq Calle Papagayo
        puntos.add(new LatLng(19.127141932551673, -96.22257572173189)); //Av Gaviota Esq Calle Garza
        puntos.add(new LatLng(19.127442441678976, -96.22333715908975)); //Av Gaviota Esq Correcaminos
        puntos.add(new LatLng(19.132030309148597, -96.22452377514395)); //Calle Correcaminos Esq Calle Anabe
        puntos.add(new LatLng(19.13237707836943, -96.22609232873876)); //Terminal

        return  puntos;
    }
}
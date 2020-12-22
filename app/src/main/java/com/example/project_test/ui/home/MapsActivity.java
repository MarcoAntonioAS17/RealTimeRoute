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


        ruta_camion = get_route_pouints2();

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
                .color(Color.GREEN)
                .addAll(ruta_camion));


        //polyline1.setTag("Ruta Matacocuite");

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

    private ArrayList<LatLng> get_route_pouints(){

        ArrayList<LatLng> puntos = new ArrayList<>();

        puntos.add(new LatLng(19.91954293785951, -96.8667806490291));
        puntos.add(new LatLng(19.91973120965887, -96.86620194388682));
        puntos.add(new LatLng(19.920155378699953, -96.86530608694218));
        puntos.add(new LatLng(19.920312244394616, -96.86466093902305));
        puntos.add(new LatLng(19.920382833906444, -96.86381279197722));
        puntos.add(new LatLng(19.920547542645192, -96.86344016343183));
        puntos.add(new LatLng(19.920866501927378, -96.86308143894452));
        puntos.add(new LatLng(19.921261259522634, -96.86273479160943));
        puntos.add(new LatLng(19.922097470974837, -96.86244005698806));
        puntos.add(new LatLng(19.922259563527533, -96.86225652352942));
        puntos.add(new LatLng(19.921254287066972, -96.8592136207038 ));
        //seguro
        puntos.add(new LatLng(19.922839198330244, -96.85860929512927));
        puntos.add(new LatLng(19.9244808977876, -96.85803744258286));
        //Cabeza Benito
        puntos.add(new LatLng(19.924619787483305, -96.85800699386904));
        puntos.add(new LatLng(19.924666437430066, -96.85801601571016));
        puntos.add(new LatLng(19.92473323165029, -96.85796188466313));
        //Justa Garcia Esq Venustian Carranza
        puntos.add(new LatLng(19.92382031865323, -96.85555965436501));
        //Venustiano Carranza Esq Xalapa
        puntos.add(new LatLng(19.923459424514558, -96.85454510215185));
        //Carranza Esq Comonfort
        puntos.add(new LatLng(19.925280474584643, -96.85457778842867));
        //Comonfort Esq Xalapa
        puntos.add(new LatLng(19.925640599718566, -96.85460110487982));
        // Comonfort Esq Reforma
        puntos.add(new LatLng(19.92655500070444, -96.8546177594937));
        //Comonfort Esq Elektra
        puntos.add(new LatLng(19.92728897783141, -96.85465279490779));
        //Comonfort Esq Similares
        puntos.add(new LatLng(19.927993560743047, -96.85466278767551));
        //Comonfort Esq Alatriste
        puntos.add(new LatLng(19.92795911454039, -96.8549858871656));
        //Alatriste Esq Melchor Ocampo
        puntos.add(new LatLng(19.928563593333823, -96.85499223953585));
        //Melchor Ocampo
        puntos.add(new LatLng(19.92880149358524, -96.85496443143518));
        //Melchor Ocampo
        puntos.add(new LatLng(19.929491663704848, -96.85483929498612));
        //Melchor Esq Bojalil
        puntos.add(new LatLng(19.92962524780315, -96.8542217545049));
        //Bojalil Esq Benito Juarez
        puntos.add(new LatLng(19.92970639166942, -96.85358117063208));
        //Bojalil Esq Faco I Madero
        puntos.add(new LatLng(19.930287948788095, -96.85301196326));
        //Francisco esq Ferrer Guardia
        puntos.add(new LatLng(19.930996634309828, -96.85224120329302));
        //Francisco Esq Santos Degollado
        puntos.add(new LatLng(19.931416477869774, -96.85188515799305));
        //Francisco Esq Bocanegra
        puntos.add(new LatLng(19.9315427316093, -96.85183176237912));
        //Francisco Casa de la Cultura
        puntos.add(new LatLng(19.932981011157413, -96.85135176654198));
        //Francisco Esq Alfoso Arroyo
        puntos.add(new LatLng(19.933072738974086, -96.85129366375192));
        //Francisco Esq Alfoso Arroyo
        puntos.add(new LatLng(19.93511670979447, -96.85062418793231));
        //Av Manuel Avila Esq Rosendo Alvarez
        puntos.add(new LatLng(19.936218824254844, -96.85025930668279));
        //Manuel Avila Esq Unca Tejeda
        puntos.add(new LatLng(19.93676442987442, -96.8501316214066));
        //Manuel Avila Templo
        puntos.add(new LatLng(19.937447525436195, -96.8500898335031));
        //Manuel Despues de Carolino Anaya
        puntos.add(new LatLng(19.93799312680376, -96.85017573086475));
        //Manuel Esq Socrates
        puntos.add(new LatLng(19.93825283239737, -96.85027091442444));
        //Manuel Esq Del Pocito
        puntos.add(new LatLng(19.938797565932184, -96.85060502498808));
        //Manuel Lab del Angel
        puntos.add(new LatLng(19.939170754276425, -96.85094629287744));
        //Manuel Esq Manuela
        puntos.add(new LatLng(19.939851400241537, -96.8516868277463));
        //Manuel Esq Mat. Enriques
        puntos.add(new LatLng(19.94077449310811, -96.85273870658165));
        //Manuel Esq Carriles
        puntos.add(new LatLng(19.94275978098019, -96.85496272543185));
        //Manuel Esq Gabriela Mitral
        puntos.add(new LatLng(19.94381418436043, -96.8537296102019));
        //Gabriela Esq Isabela Catolica
        puntos.add(new LatLng(19.944793082530996, -96.85258850995484));
        //Gabriela Tecnica
        puntos.add(new LatLng(19.945967002219913, -96.85120402818755));
        //Gabriela Esq Miguel Hidalgo
        puntos.add(new LatLng(19.94669835353429, -96.85036216750035));
        //Gabriela Esq Carlos Carballal
        puntos.add(new LatLng(19.94784866420303, -96.84899794876381));
        //Gabriela Esq Super GiOS
        puntos.add(new LatLng(19.94940256168473, -96.84722333716583));
        //Gabriela Monte
        puntos.add(new LatLng(19.950250160339753, -96.84624582121279));
        puntos.add(new LatLng(19.950400177331225, -96.84601440926227));
        puntos.add(new LatLng(19.950834087258958, -96.84513310009034));
        //Entrada Tec
        puntos.add(new LatLng(19.950845432830647, -96.84479916507334));
        //Vuelta Tec
        puntos.add(new LatLng(19.950866233043293, -96.84473814481923));
        puntos.add(new LatLng(19.950940609538783, -96.84472540432661));
        puntos.add(new LatLng(19.950998597968574, -96.84478038961053));
        //Fin

        return  puntos;
    }

    private ArrayList<LatLng> get_route_pouints2(){

        ArrayList<LatLng> puntos = new ArrayList<>();

        puntos.add(new LatLng(19.950998597968574, -96.84478038961053));
        puntos.add(new LatLng(19.950834087258958, -96.84513310009034));
        //Entrada Tec
        puntos.add(new LatLng(19.950400177331225, -96.84601440926227));
        puntos.add(new LatLng(19.950400177331225, -96.84601440926227));
        puntos.add(new LatLng(19.950250160339753, -96.84624582121279));
        puntos.add(new LatLng(19.94940256168473, -96.84722333716583));
        //Gabriela Monte
        puntos.add(new LatLng(19.94784866420303, -96.84899794876381));
        //Gabriela Esq Super GiOS
        puntos.add(new LatLng(19.94669835353429, -96.85036216750035));
        //Gabriela Esq Carlos Carballal
        puntos.add(new LatLng(19.945967002219913, -96.85120402818755));
        //Gabriela Esq Miguel Hidalgo
        puntos.add(new LatLng(19.944793082530996, -96.85258850995484));
        //Gabriela Tecnica
        puntos.add(new LatLng(19.94381418436043, -96.8537296102019));
        //Gabriela Esq Isabela Catolica
        puntos.add(new LatLng(19.942701266511246, -96.85500330222682));
        //Gabriela Esq Manuel Avila
        puntos.add(new LatLng(19.941741106245125, -96.85395582368486));
        //Manuel Unidad
        puntos.add(new LatLng(19.94071858065521, -96.85279846461482));
        //Manuel Carriles
        puntos.add(new LatLng(19.93979421181931, -96.8517542308609));
        //Manuel Esq Enriques
        puntos.add(new LatLng(19.939102976729888, -96.85097975750448));
        //Manuel Esq Manuela
        puntos.add(new LatLng(19.938722791240192, -96.85064999960032));
        //Manuel
        puntos.add(new LatLng(19.93844345464908, -96.85045478502707));
        //Manel Laboratorio del Angel
        puntos.add(new LatLng(19.93822212203623, -96.85036364496867));
        //Manuel Esq de los Pocitos
        puntos.add(new LatLng(19.937975470601895, -96.85024215084623));
        //Manuel Esq Socrates
        puntos.add(new LatLng(19.93757909841644, -96.85017268279036));
        puntos.add(new LatLng(19.937241208559175, -96.85016826354203));
        //Manuel esq Carolino Anaya
        puntos.add(new LatLng(19.936808010132857, -96.85018414251844));
        puntos.add(new LatLng(19.936239202602117, -96.85032338427433));
        //Manuel Esq Unca Tejeda
        puntos.add(new LatLng(19.935151807938883, -96.85069986025093));
        //Manuel Ganadera
        puntos.add(new LatLng(19.934022950147156, -96.85106218527369));
        puntos.add(new LatLng(19.93310370828819, -96.85138917698441));
        //Manuel Esq Modatelas
        puntos.add(new LatLng(19.932977462695185, -96.85135476802559));
        puntos.add(new LatLng(19.932981011157413, -96.85135176654198));
        //Francisco Esq Alfoso Arroyo
        puntos.add(new LatLng(19.9315427316093, -96.85183176237912));
        //Francisco Casa de la Cultura
        puntos.add(new LatLng(19.931416477869774, -96.85188515799305));
        //Francisco Esq Bocanegra
        puntos.add(new LatLng(19.930996634309828, -96.85224120329302));
        //Francisco Esq Santos Degollado
        puntos.add(new LatLng(19.930857704688226, -96.85202678079568));
        //Degollado esq 5 de Mayo
        puntos.add(new LatLng(19.9299740120968, -96.8526151547144));
        //5 de mayo esq Ferrer
        puntos.add(new LatLng(19.929360983241175, -96.85299205090064));
        //5 de Mayo esq Bojalil
        puntos.add(new LatLng(19.92881221256291, -96.85329548179959));
        //5 de Mayo esq Galvan
        puntos.add(new LatLng(19.92887925542045, -96.85417703117808));
        //Galvan esq Benito Juarez
        puntos.add(new LatLng(19.928390051463857, -96.85412548963434));
        //Benito Juarez Iglesia
        puntos.add(new LatLng(19.928335668234183, -96.85439616272143));
        puntos.add(new LatLng(19.928267116900702, -96.85445005837678));
        //Taqueria Parroquia
        puntos.add(new LatLng(19.92802873921112, -96.8544453867964));
        puntos.add(new LatLng(19.927990657993533, -96.85464412276431));
        //Alatriste Esq Comonfort
        puntos.add(new LatLng(19.92794305645854, -96.85497830298587));
        //Alatriste esq Melchor
        puntos.add(new LatLng(19.92787522424665, -96.855656790091));
        //alatriste Esq Pino Suarez
        puntos.add(new LatLng(19.927287305842576, -96.85559738931666));
        //Pino Suarez Esq Rojas
        puntos.add(new LatLng(19.926863942321695, -96.8555495286034));
        puntos.add(new LatLng(19.92643444194239, -96.85551036983841));
        //Pino Suarez esq Lerdo
        puntos.add(new LatLng(19.925139860400407, -96.85543240104147));
        //Pino Suarez Esq Reforma
        puntos.add(new LatLng(19.925651880738414, -96.85460113432978));
        //Reforma Esq Comonfort
        puntos.add(new LatLng(19.92585750374074, -96.85459238767348));
        //Comonfort Esq Mina
        puntos.add(new LatLng(19.92584690540791, -96.85385063980928));
        //Mina esq Zamora
        puntos.add(new LatLng(19.92597808047412, -96.85318343020862));
        //Mina Esq Morelos
        puntos.add(new LatLng(19.925646443129907, -96.8532223703684));
        //Morelos
        puntos.add(new LatLng(19.924644082710845, -96.85351440163043));
        //Morelos esq LAvoignet
        puntos.add(new LatLng(19.92326653157662, -96.85398268237932));
        //Morelos Esq Carranza
        puntos.add(new LatLng(19.923459424514558, -96.85454510215185));
        //Carranza Esq Comonfort
        puntos.add(new LatLng(19.92382031865323, -96.85555965436501));
        //Venustiano Carranza Esq Xalapa
        puntos.add(new LatLng(19.92473323165029, -96.85796188466313));
        //Justa Garcia Esq Venustian Carranza
        puntos.add(new LatLng(19.924666437430066, -96.85801601571016));
        puntos.add(new LatLng(19.924619787483305, -96.85800699386904));
        puntos.add(new LatLng(19.9244808977876, -96.85803744258286));
        //Cabeza Benito
        puntos.add(new LatLng(19.922839198330244, -96.85860929512927));
        puntos.add(new LatLng(19.921254287066972, -96.8592136207038 ));
        //seguro
        puntos.add(new LatLng(19.922259563527533, -96.86225652352942));
        puntos.add(new LatLng(19.922097470974837, -96.86244005698806));
        puntos.add(new LatLng(19.921261259522634, -96.86273479160943));
        puntos.add(new LatLng(19.920866501927378, -96.86308143894452));
        puntos.add(new LatLng(19.920547542645192, -96.86344016343183));
        puntos.add(new LatLng(19.920382833906444, -96.86381279197722));
        puntos.add(new LatLng(19.920312244394616, -96.86466093902305));
        puntos.add(new LatLng(19.920155378699953, -96.86530608694218));
        puntos.add(new LatLng(19.91973120965887, -96.86620194388682));
        puntos.add(new LatLng(19.91954293785951, -96.8667806490291));

        return  puntos;
    }
}
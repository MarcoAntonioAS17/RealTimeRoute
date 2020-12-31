package com.example.project_test;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.project_test.models.OnBusListener;
import com.example.project_test.ui.routes.MapaRutas;
import com.example.project_test.ui.routes.RoutesFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity implements OnBusListener {

    private AppBarConfiguration mAppBarConfiguration;
    FloatingActionButton fab_gps;
    FloatingActionButton fab_go;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab_gps = findViewById(R.id.fab_gps);
        fab_gps.setOnClickListener(view ->
                Snackbar.make(view,"GO Float Action Button", Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show()
        );

        fab_go = findViewById(R.id.fab_go);
        fab_go.setOnClickListener(view ->
                Snackbar.make(view,"GO Float Action Button", Snackbar.LENGTH_LONG)
                .setAction("Action",null).show()
        );

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_routes, R.id.nav_real_time_routes, R.id.nav_report_problem, R.id.nav_config)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public void hideFloatingActionButton(){
        fab_gps.hide();
        fab_go.hide();
    }

    public void showFloatingActionButton(){
        fab_gps.show();
        fab_go.show();
    }

    @Override
    public boolean onSupportNavigateUp() {

        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed ();

        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate();

    }


    @Override
    public void onBusClick(String ruta) {
        MapaRutas mapaRutas = MapaRutas.instance(ruta);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction ();
//        transaction.addToBackStack ("Frag");

        transaction.replace(R.id.nav_host_fragment, mapaRutas);

        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }
}
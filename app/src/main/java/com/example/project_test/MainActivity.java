package com.example.project_test;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;
    NavController navController;
    
    private SensorManager sensorManager;
    private Sensor mRotationVectorSensor;
    private int ite = 1;
    private long time=0, delta_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get an instance of the SensorManager
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mRotationVectorSensor = sensorManager.getDefaultSensor(
                Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, mRotationVectorSensor, 10000000);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_routes, R.id.nav_real_time_routes, R.id.nav_report_problem, R.id.nav_config)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // we received a sensor event. it is a good practice to check
        // that we received the proper event
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            if (Math.abs(event.values[1]) > 2)
            {

                time = event.timestamp;
                ite++;
            }

            if (time != 0)
                delta_time = event.timestamp - time;
            if (delta_time > 500000000*3){
                time = 0;
                ite = 0;
            }
            if (ite > 4){
//                Log.d("TYAM","=>Saliendo");
                finish();
            }

     //       Log.d("TYAM","tiempo=>"+delta_time);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
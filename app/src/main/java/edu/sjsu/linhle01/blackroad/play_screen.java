package edu.sjsu.linhle01.blackroad;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class play_screen extends AppCompatActivity implements OnMapReadyCallback,SensorEventListener {

    private GoogleMap mMap;

    String select;
    private LatLng la, sydney, barcelona, hcm;

    // define the display assembly compass picture
    private ImageView image;
        // record the compass picture angle turned
    private float currentDegree = 0f;
        // device sensor manager
    private SensorManager mSensorManager;
    TextView tvHeading;

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        image = (ImageView) findViewById(R.id.compass);
        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {
        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);
        //tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");
        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                        -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        // how long the animation will take place
        ra.setDuration(210);
        // set the animation after the end of the reservation status
        ra.setFillAfter(true);
        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
       mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        la = new LatLng(34.0522, -118.2437);
        mMap.addMarker(new MarkerOptions().position(la).title("Los Angeles").snippet("Current Traffic:"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(la));

        sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Sydney").snippet("Current Traffic:"));

        barcelona = new LatLng(41.3851, 2.1734);
        mMap.addMarker(new MarkerOptions().position(barcelona).title("Barcelona").snippet("Current Traffic:"));

        hcm = new LatLng(10.8231,106.6297);
        mMap.addMarker(new MarkerOptions().position(hcm).title("Ho Chi Minh city").snippet("Current Traffic:"));
    }

    public void update(View view)
    {
        Spinner loca = (Spinner) findViewById(R.id.location);
        //String gen = gender.getSelectedItem().toString();
        select = loca.getSelectedItem().toString();

        editor.putString("location",select);
        editor.commit();

        if(select.equals("Los Angeles"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(la, 6));
        else if(select.equals("Sydney"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 6));
        else if(select.equals("Ho Chi Minh city"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcm, 6));
        else
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(barcelona, 6));
    }


    public void enter(View view)
    {
        Intent intent = new Intent(this, main_play.class);
        startActivity(intent);
    }


}




























package com.example.hazril.speedwarning;

import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.UiSettings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import com.google.android.gms.maps.model.Marker;
import java.io.IOException;
import java.util.List;
import android.content.Intent;
//import android.content.pm.ActivityInfo;

import android.support.v7.widget.AlertDialogLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//import android.view.View.OnClickListener;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import android.view.ViewGroup;
//import android.view.LayoutInflater;
//import android.app.Fragment;
//import android.widget.Button;
//import android.content.DialogInterface.OnClickListener;

//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker marker;
    LocationListener locationListener;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//  Account Button
        button = (Button) findViewById(R.id.btnAccount);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    openLogin();
            }
            public void openLogin(){
                Intent intent = new Intent(MapActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

//  Setting button
        button = (Button) findViewById(R.id.btnSetting);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openAccount();
            }
            public void openAccount(){
                Intent intent = new Intent(MapActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });


//Orientation Landscape
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//Live location

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

//check the network provider is enable.

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);

                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
//                Location MMU = new Location("Multimedia University");//provider name is unnecessary
//                MMU.setLatitude(2.242665696d);//your coords of course
//                MMU.setLongitude(102.272832242d);

                //get the location name from latitude and longitude
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> addresses =
                            geocoder.getFromLocation(latitude, longitude, 1);
                    String result = addresses.get(0).getLocality() + ":";
                    result += addresses.get(0).getCountryName();
                    LatLng latLng = new LatLng(latitude, longitude);
                    if (marker != null) {
                        marker.remove();
                        marker = mMap.addMarker(new MarkerOptions().position(latLng).title(result));
                        mMap.setMaxZoomPreference(100);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21.0f));
                    } else {
                        marker = mMap.addMarker(new MarkerOptions().position(latLng).title(result));
                        mMap.setMaxZoomPreference(100);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21.0f));
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

                TextView speed = (TextView) findViewById(R.id.Speed);
                if (location == null) {
                    speed.setText("-km/h");
                } else {
                    float nCurrentSpeed = location.getSpeed();
                    speed.setText(nCurrentSpeed * 3.6 + "km/h");
                }

                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(2.2580, 102.2784))
                        .radius(200)
                        .strokeColor(Color.RED));


//
//                Circle circleMMU = mMap.addCircle(new CircleOptions());
//                circleMMU.setCenter(new LatLng(2.242665696d, 102.272832242d));
//                circleMM.setRadius(5000);
//                circleMMU.setFillColor(Color.BLUE);
//                float nCurrentSpeed = location.getSpeed();
//                float[] distance = new float[2];



//                AlertDialog alertDialog = new AlertDialog.Builder(MapActivity.this).create();
//                alertDialog.setTitle("Warning!");
//                alertDialog.setMessage("You are currently speeding");
//                alertDialog.setCancelable(true);

//                Location.distanceBetween(location.getLatitude(), location.getLongitude(),circleMMU.getCenter().latitude, circleMMU.getCenter().longitude, distance);

//                if ((nCurrentSpeed * 3.6 > 30)&&(distance[2]>circleMMU.getRadius())){
//                        alertDialog.show();
//                }
//                else{
//                    alertDialog.hide();
//                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //navigator.geolocation.getCurrentPosition(successCallback);
        //mMap.setMyLocationEnabled(true);
        //mMap.setOnMyLocationButtonClickListener(this);
        //mMap.setOnMyLocationClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

}
